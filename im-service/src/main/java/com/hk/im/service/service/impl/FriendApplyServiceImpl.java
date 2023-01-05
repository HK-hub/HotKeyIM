package com.hk.im.service.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.common.resp.PageResult;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.domain.constant.FriendConstants;
import com.hk.im.domain.entity.*;
import com.hk.im.domain.request.ApplyHandleRequest;
import com.hk.im.domain.request.FriendApplyRequest;
import com.hk.im.domain.request.FriendFindRequest;
import com.hk.im.domain.vo.FriendApplyVO;
import com.hk.im.domain.vo.UserVO;
import com.hk.im.infrastructure.event.friend.event.ApplyHandleEvent;
import com.hk.im.infrastructure.event.friend.event.FriendApplyEvent;
import com.hk.im.infrastructure.mapper.FriendApplyMapper;
import com.hk.im.infrastructure.mapper.FriendMapper;
import com.hk.im.infrastructure.mapper.UserMapper;
import com.hk.im.infrastructure.mapstruct.FriendApplyMapStructure;
import com.hk.im.infrastructure.mapstruct.UserMapStructure;
import com.hk.im.service.service.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : HK意境
 * @ClassName : FriendApplyServiceImpl
 * @date : 2023/1/2 14:03
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class FriendApplyServiceImpl extends ServiceImpl<FriendApplyMapper, FriendApply> implements FriendApplyService {

    @Resource
    private FriendService friendService;
    @Resource
    private FriendMapper friendMapper;
    @Resource
    private GroupService groupService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private UserService userService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;


    /**
     * 发现好友
     *
     * @param request
     *
     * @return
     */
    @Override
    public PageResult findFriendAccountOrName(FriendFindRequest request) {

        // 参数校验
        if (Objects.isNull(request) || StringUtils.isEmpty(request.getSearchKey())) {
            return PageResult.SUCCESS(null, request.getCurrentPage());
        }

        // 账号查找
        User byAccount = this.userMapper.getUserByAccountOrPhoneOrEmail(request.getSearchKey());
        // 昵称查找
        Page<User> userPage = this.userService.lambdaQuery().like(User::getUsername, request.getSearchKey())
                .page(Page.of(request.getCurrentPage(), request.getPageSize()));
        List<User> userList = userPage.getRecords();

        // 组装好友查询列表
        if (CollectionUtils.isEmpty(userList)) {
            userList = new ArrayList<User>();
        }
        if (Objects.nonNull(byAccount)) {
            userList.add(byAccount);
        }

        // 封装响应信息
        List<UserVO> userVOList = this.convertUserToVO(userList);
        return PageResult.SUCCESS(userVOList, request.getCurrentPage());
    }


    /**
     * 发现群聊
     *
     * @param request
     *
     * @return
     */
    @Override
    public PageResult findGroupAccountOrName(FriendFindRequest request) {

        // 参数校验
        if (Objects.isNull(request) || StringUtils.isEmpty(request.getSearchKey())) {
            return PageResult.SUCCESS(null, request.getCurrentPage());
        }

        // 根据群号搜索
        Group group = this.groupService.lambdaQuery().eq(Group::getGroupAccount, request.getSearchKey()).one();
        List<Group> records = this.groupService.lambdaQuery().like(Group::getGroupName, request.getSearchKey())
                .page(Page.of(request.getCurrentPage(), request.getPageSize())).getRecords();
        if (Objects.isNull(records)) {
            records.add(group);
        }

        return PageResult.SUCCESS(records, request.getCurrentPage());
    }


    /**
     * 好友申请
     *
     * @param request
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult applyToBeFriend(FriendApplyRequest request) {

        // 参数校验
        if (Objects.isNull(request) || StringUtils.isEmpty(String.valueOf(request.getFromUserId())) ||
                StringUtils.isEmpty(String.valueOf(request.getToUserId()))) {
            return ResponseResult.FAIL("请补充好完整好友申请信息哦!");
        }

        // 前置校验：接收者是否本人，是否已经成为好友, 问题答案，是否允许加好友
        Long fromUserId = request.getFromUserId();
        Long toUserId = request.getToUserId();
        if (Objects.equals(fromUserId, toUserId)) {
            return ResponseResult.FAIL("好友申请失败，您和自己已经是好友了哦!");
        }

        // 检查好友列表
        Friend positive = this.friendService.isFriendRelationship(fromUserId, toUserId);
        Friend negative = this.friendService.isFriendRelationship(toUserId, fromUserId);

        // 双向好友关系
        if (Objects.nonNull(positive) && Objects.nonNull(negative)) {
            // 两人是好友，不能加
            return ResponseResult.FAIL("你们已经是好友关系了哦,无需重复添加!");
        }

        // 黑名单: a 加 b, 那么需要检查 b 的黑名单里面是否有a: 此时 b 是主体，a 是受体
        Friend blackList = this.friendService.isFriendBlackList(toUserId, fromUserId);
        if (Objects.nonNull(blackList)) {
            // 黑名单，添加失败
            return ResponseResult.FAIL("抱歉，对方已把您加入黑名单,无法申请好友!");
        }

        // 查询目标好友信息
        UserInfo userInfo = this.userInfoService.getById(toUserId);

        // 加好友策略：
        FriendConstants.FriendApplyStatus status = null;
        FriendConstants.FriendApplyPolicy applyPolicy = FriendConstants.FriendApplyPolicy.values()[userInfo.getAddFriendPolicy()];
        if (Objects.equals(applyPolicy, FriendConstants.FriendApplyPolicy.PASSWORD) ||
                Objects.equals(applyPolicy, FriendConstants.FriendApplyPolicy.PROBLEM)) {
            // 输入密码，答案模式，校验
            if (!Objects.equals(userInfo.getAddFriendAnswer(), request.getAnswer())) {
                // 答案，密码错误
                return ResponseResult.FAIL("对不起，您输入的答案或密码错误!，请重试哦!");
            }
            // 等待处理
            status = FriendConstants.FriendApplyStatus.AGREE;
        } else if (Objects.equals(applyPolicy, FriendConstants.FriendApplyPolicy.VERIFY)) {
            // 待验证
            status = FriendConstants.FriendApplyStatus.WAITING;
        } else if (Objects.equals(applyPolicy, FriendConstants.FriendApplyPolicy.AGREE)) {
            // 直接同意
            status = FriendConstants.FriendApplyStatus.AGREE;
        }

        // 检查是否有申请记录
        FriendApply apply = this.getFriendApply(fromUserId, toUserId);
        if (Objects.isNull(apply)) {
            // 没有申请记录
            apply = new FriendApply();
            apply.setSenderId(fromUserId);
            apply.setAcceptorId(toUserId);
        }

        // 发送加好友申请
        apply.setApplyInfo(request.getApplyInfo());
        // 添加类型：
        apply.setApplyType(FriendConstants.FriendApplyType.FRIEND.ordinal());
        // 如果已经存在申请记录但是，被拒绝了，或者被忽略了， 则重新申请
        apply.setStatus(status.ordinal());

        // 保存或更新
        boolean update = this.saveOrUpdate(apply);

        // 添加好友申请保存成功，发布相关事件: 通知接收者
        if (BooleanUtils.isTrue(update)) {
            applicationEventPublisher.publishEvent(new FriendApplyEvent(this, apply));
        }

        return ResponseResult.SUCCESS(apply);
    }


    /**
     * 获取一个好友申请记录
     *
     * @param fromUserId
     * @param toUserId
     *
     * @return
     */
    @Override
    public FriendApply getFriendApply(Long fromUserId, Long toUserId) {

        LambdaQueryChainWrapper<FriendApply> queryWrapper = this.lambdaQuery()
                .eq(FriendApply::getSenderId, fromUserId).eq(FriendApply::getAcceptorId, toUserId);
        return queryWrapper.one();
    }


    /**
     * 获取用户带验证的好友申请: 待处理，被拒绝
     *
     * @param userId
     *
     * @return
     */
    @Override
    public ResponseResult getUserAllSendFriendApply(Long userId) {

        List<FriendApply> applyList = this.lambdaQuery().eq(FriendApply::getSenderId, userId)
                .and(wrapper -> {
                    wrapper.eq(FriendApply::getStatus, FriendConstants.FriendApplyStatus.WAITING.ordinal())
                            .or()
                            .eq(FriendApply::getStatus, FriendConstants.FriendApplyStatus.REJECT.ordinal());
                }).list();

        if (CollectionUtils.isEmpty(applyList)) {
            // 没有有效申请列表
            applyList = Collections.EMPTY_LIST;
        }

        // 组装信息
        List<Long> acceptorIdList = applyList.stream().map(FriendApply::getAcceptorId).toList();
        List<UserVO> userAndInfoList = (List<UserVO>) this.userService.getUserAndInfoList(acceptorIdList).getData();
        Map<Long, UserVO> userVOMap = userAndInfoList.stream().collect(Collectors.toMap(UserVO::getId, value -> value));

        // 收集 acceptor
        List<FriendApplyVO> friendApplyVOS = applyList.stream().map(apply -> {
            FriendApplyVO applyVO = FriendApplyMapStructure.INSTANCE.toVO(apply,
                    null, userVOMap.get(apply.getAcceptorId()));
            return applyVO;
        }).toList();


        return ResponseResult.SUCCESS(friendApplyVOS);
    }


    /**
     * 获取用户所有收到的有效好友申请：待验证，同意
     *
     * @param userId
     *
     * @return
     */
    @Override
    public ResponseResult getUserAllReceiveFriendApply(Long userId) {

        // 查询所有待处理和已经同意的列表
        List<FriendApply> applyList = this.lambdaQuery().eq(FriendApply::getAcceptorId, userId)
                .and(wrapper -> {
                    wrapper.eq(FriendApply::getStatus,FriendConstants.FriendApplyStatus.WAITING.ordinal())
                            .or()
                            .eq(FriendApply::getStatus,FriendConstants.FriendApplyStatus.AGREE.ordinal());
                }).list();

        if (CollectionUtils.isEmpty(applyList)) {
            // 没有有效申请列表
            applyList = Collections.EMPTY_LIST;
        }


        // 组装信息
        List<Long> senderList = applyList.stream().map(FriendApply::getSenderId).toList();
        List<UserVO> userAndInfoList = (List<UserVO>) this.userService.getUserAndInfoList(senderList).getData();
        Map<Long, UserVO> userVOMap = userAndInfoList.stream().collect(Collectors.toMap(UserVO::getId, value -> value));

        // 收集sender
        List<FriendApplyVO> friendApplyVOS = applyList.stream().map(apply -> {
            FriendApplyVO applyVO = FriendApplyMapStructure.INSTANCE.toVO(apply,
                    userVOMap.get(apply.getSenderId()), null);
            return applyVO;
        }).toList();

        return ResponseResult.SUCCESS(friendApplyVOS);
    }


    /**
     * 处理好友申请消息
     * @param request
     * @return
     */
    @Override
    public ResponseResult handleFriendApply(ApplyHandleRequest request) {

        // 参数校验
        Boolean preConditionCheck = Objects.isNull(request) || Objects.isNull(request.getSenderId())
                || Objects.isNull(request.getAcceptorId()) || Objects.isNull(request.getOperation());
        // 判断校验结果
        if (BooleanUtils.isTrue(preConditionCheck)) {
            return ResponseResult.FAIL("申请信息不完整!");
        }

        // 处理加好友处理
        Integer type = request.getType();
        Long senderId = request.getSenderId();
        Long acceptorId = request.getAcceptorId();
        if (Objects.equals(type, FriendConstants.FriendApplyType.FRIEND.ordinal())) {
            // 加好友: 需要二次校验
            // 看接收者好友列表里面是否有发起者
            Friend positiveRelationShip = this.friendService.isFriendRelationship(senderId, acceptorId);
            Friend negativeRelationShip = this.friendService.isFriendRelationship(senderId, acceptorId);
            if (Objects.nonNull(positiveRelationShip) && Objects.nonNull(negativeRelationShip)) {
                return ResponseResult.SUCCESS("你们已经是好友呢，请勿重复操作");
            }

            // 看接收者的申请操作
            FriendConstants.FriendApplyStatus operation =
                    FriendConstants.FriendApplyStatus.values()[request.getOperation()];

            // 发布事件
            applicationEventPublisher.publishEvent(new ApplyHandleEvent(this, request));

            // 判断操作
            ResponseResult result = null;
            if (FriendConstants.FriendApplyStatus.REJECT.equals(operation)) {
                // 拒绝申请: 发布事件，响应请求
                result = ResponseResult.SUCCESS("您已拒绝该好友的申请");
            } else if (FriendConstants.FriendApplyStatus.IGNORE.equals(operation)) {
                // 拒绝申请: 发布事件，响应请求
                result = ResponseResult.SUCCESS("您已忽略该好友的申请");
            } else if (FriendConstants.FriendApplyStatus.AGREE.equals(operation)) {
                // 同意添加为好友
                // 不是好友，进行加好友操作
                result = this.toBeFriend(senderId, acceptorId);
            }
            return result;

        } else if (Objects.equals(type, FriendConstants.FriendApplyType.GROUP.ordinal())) {
            // 加群
        }

        // 不支持的操作
        return ResponseResult.FAIL().setResultCode(ResultCode.NO_SUPPORT_OPERATION);
    }


    /**
     * 成为好友
     * @param senderId
     * @param acceptorId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult toBeFriend(Long senderId, Long acceptorId) {

        // 接收者为主体
        Friend acceptor = new Friend();
        acceptor.setUserId(acceptorId);
        acceptor.setFriendId(senderId);
        acceptor.setRelation(FriendConstants.FriendRelationship.FRIEND.ordinal());

        // 发送者为受体
        Friend sender = new Friend();
        sender.setUserId(senderId);
        sender.setFriendId(acceptorId);
        sender.setRelation(FriendConstants.FriendRelationship.FRIEND.ordinal());

        this.friendService.saveBatch(List.of(acceptor, sender));

        // 组装用户信息
        FriendApplyVO applyVO = new FriendApplyVO();
        UserVO senderUser = (UserVO) this.userService.getUserAndInfo(senderId).getData();
        UserVO acceptorUser = (UserVO) this.userService.getUserAndInfo(acceptorId).getData();
        applyVO.setSender(senderUser);
        applyVO.setAcceptor(acceptorUser);

        return ResponseResult.SUCCESS(applyVO).setMessage("您已同意好友申请");
    }


    /**
     * 转换组装信息
     *
     * @param userList
     *
     * @return
     */
    private List<UserVO> convertUserToVO(List<User> userList) {

        // 判空
        if (CollectionUtils.isEmpty(userList)) {
            return Collections.EMPTY_LIST;
        }

        // 收集用户id 列表
        List<Long> userIdList = userList.stream().map(User::getId).toList();

        // 查询 info 信息，组装 vo
        Map<Long, UserInfo> userInfoMap = this.userInfoService.listByIds(userIdList).stream()
                .collect(Collectors.toMap(UserInfo::getUserId, value -> value));

        List<UserVO> userVOList = new ArrayList<>();
        for (User user : userList) {
            userVOList.add(UserMapStructure.INSTANCE.toVo(user, userInfoMap.get(user.getId())));
        }

        return userVOList;
    }


}





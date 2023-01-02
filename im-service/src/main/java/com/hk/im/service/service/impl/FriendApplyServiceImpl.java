package com.hk.im.service.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.common.resp.PageResult;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.constant.FriendConstants;
import com.hk.im.domain.entity.*;
import com.hk.im.domain.request.FriendApplyRequest;
import com.hk.im.domain.request.FriendFindRequest;
import com.hk.im.domain.vo.UserVO;
import com.hk.im.infrastructure.event.friend.event.FriendApplyEvent;
import com.hk.im.infrastructure.mapper.FriendApplyMapper;
import com.hk.im.infrastructure.mapper.UserMapper;
import com.hk.im.infrastructure.mapstruct.UserMapStructure;
import com.hk.im.service.service.*;
import org.apache.commons.collections4.CollectionUtils;
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
    private FriendApplyService friendApplyService;
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
        Friend friend = this.friendService.isFriendRelationship(fromUserId, toUserId);
        if (Objects.nonNull(friend)) {
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
        FriendApply apply = this.friendApplyService.getFriendApply(fromUserId, toUserId);
        if (Objects.isNull(apply)) {
            // 没有申请记录
            apply = new FriendApply();
            apply.setSenderId(fromUserId);
            apply.setAcceptorId(toUserId);
        }

        // 发送加好友申请
        apply.setApplyInfo(request.getApplyInfo());
        apply.setApplyType(applyPolicy.ordinal());
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
        return ResponseResult.SUCCESS(applyList);
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
        return ResponseResult.SUCCESS(applyList);
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





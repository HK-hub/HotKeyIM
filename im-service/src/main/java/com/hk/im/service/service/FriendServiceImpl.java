package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.*;
import com.hk.im.common.error.ApiException;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.domain.constant.FriendConstants;
import com.hk.im.domain.constant.UserConstants;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.entity.Friend;
import com.hk.im.domain.entity.FriendGroup;
import com.hk.im.domain.entity.User;
import com.hk.im.domain.request.ModifyFriendInfoRequest;
import com.hk.im.domain.request.ModifyFriendStatusRequest;
import com.hk.im.domain.request.friend.ModifyFriendGroupRequest;
import com.hk.im.domain.response.FriendListResponse;
import com.hk.im.domain.vo.FriendVO;
import com.hk.im.domain.vo.UserVO;
import com.hk.im.infrastructure.mapper.FriendMapper;
import com.hk.im.infrastructure.mapstruct.FriendMapStructure;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : HK意境
 * @ClassName : FriendServiceImpl
 * @date : 2023/1/2 16:48
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend>
        implements FriendService {

    @Resource
    private FriendMapper friendMapper;
    @Resource
    private UserService userService;
    @Resource
    private FriendGroupService friendGroupService;
    @Resource
    private AuthorizationService authorizationService;
    @Resource
    private GroupMemberService groupMemberService;


    /**
     * 是否好友关系
     *
     * @param fromUserId
     * @param toUserId
     *
     * @return Friend
     */
    @Override
    public Friend isFriendRelationship(Long fromUserId, Long toUserId) {

        Friend friend = this.friendMapper.selectFriendByTowUser(fromUserId, toUserId);
        return friend;
    }


    /**
     * 是否黑名单
     *
     * @param fromUserId
     * @param toUserId
     *
     * @return
     */
    @Override
    public Friend isFriendBlackList(Long fromUserId, Long toUserId) {
        return this.haveFriendRelationship(fromUserId, toUserId, FriendConstants.FriendRelationship.BLACK);
    }

    /**
     * 是否具有某种关系
     *
     * @param fromUserId
     * @param toUserId
     * @param relation
     *
     * @return
     */
    @Override
    public Friend haveFriendRelationship(Long fromUserId, Long toUserId, FriendConstants.FriendRelationship relation) {
        return this.friendMapper.selectTheRelationship(fromUserId, toUserId, relation.ordinal());
    }


    /**
     * 获取好友列表：注意分组，排序规则, 备注等
     *
     * @param userId
     *
     * @return
     */
    @Override
    public ResponseResult getUserFriendList(Long userId) {

        LambdaQueryChainWrapper<Friend> queryWrapper = this.lambdaQuery();

        // 好友状态为：1.好友，3.特别关心
        List<Friend> friendList = queryWrapper.eq(Friend::getUserId, userId)
                .and(wrapper -> {
                    wrapper.eq(Friend::getRelation, FriendConstants.FriendRelationship.FRIEND.ordinal())
                            .or()
                            .eq(Friend::getRelation, FriendConstants.FriendRelationship.CAREFUL.ordinal());
                }).list();

        // 根据 friendId 查询好友用户信息 UserVO
        List<Long> friendIdList = friendList.stream().map(Friend::getFriendId).toList();
        // 批量查询好友信息
        ResponseResult result = this.userService.getUserAndInfoList(friendIdList);
        if (BooleanUtils.isFalse(result.isSuccess())) {
            return ResponseResult.FAIL("查询好友信息失败");
        }

        // 组装响应数据：<group, List<FriendVO>>
        List<UserVO> userVOList = (List<UserVO>) result.getData();
        // 转换为 <id, userVO> 集合
        Map<Long, UserVO> userVOMap = userVOList.stream().collect(Collectors.toMap(UserVO::getId, value -> value));

        // <group, List<FriendVO>>
        Map<String, List<FriendVO>> userFriendMap = friendList.stream()
                .map(friend -> FriendMapStructure.INSTANCE.toVO(friend, userVOMap.get(friend.getFriendId())))
                .collect(Collectors.groupingBy(FriendVO::getGroup));

        // 排序等: 组间按照字母排序，组内好友按照在线状态排序，相同状态按照名称排序
        Map<String, List<FriendVO>> sortedFriendMap = userFriendMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        sortedFriendMap.forEach((group, friendVOList) -> {
            friendVOList.sort(Comparator.comparingInt(FriendVO::getStatus));
        });

        // 添加最后的黑名单
        ResponseResult userBlackListResult = this.getUserBlackList(userId);
        sortedFriendMap.put(UserConstants.BLACK_LIST, (List<FriendVO>) userBlackListResult.getData());

        // 返回数据
        return ResponseResult.SUCCESS(sortedFriendMap);
    }

    /**
     * 获取用户好友列表V2 版本: 只返回好友列表
     *
     * @param userId
     *
     * @return
     */
    @Override
    public ResponseResult getUserFriendListV2(Long userId) {
        LambdaQueryChainWrapper<Friend> queryWrapper = this.lambdaQuery();

        // 好友状态为：1.好友，3.特别关心
        List<Friend> friendList = queryWrapper.eq(Friend::getUserId, userId)
                .and(wrapper -> {
                    wrapper.eq(Friend::getRelation, FriendConstants.FriendRelationship.FRIEND.ordinal())
                            .or()
                            .eq(Friend::getRelation, FriendConstants.FriendRelationship.CAREFUL.ordinal());
                }).list();

        // 根据 friendId 查询好友用户信息 UserVO
        List<Long> friendIdList = friendList.stream().map(Friend::getFriendId).toList();
        // 批量查询好友信息
        ResponseResult result = this.userService.getUserAndInfoList(friendIdList);
        if (BooleanUtils.isFalse(result.isSuccess())) {
            return ResponseResult.FAIL("查询好友信息失败");
        }
        // 组装响应数据：<group, List<FriendVO>>
        List<UserVO> userVOList = (List<UserVO>) result.getData();
        // 转换为 <id, userVO> 集合
        Map<Long, UserVO> userVOMap = userVOList.stream().collect(Collectors.toMap(UserVO::getId, value -> value));

        // 构造 friendList
        List<FriendVO> friendVOList = friendList.stream().map(friend -> {
                    FriendVO friendVO = FriendMapStructure.INSTANCE.toVO(friend, userVOMap.get(friend.getFriendId()));
                    // 设置在线状态：
                    Boolean onlineStatus = this.authorizationService.getUserOnlineStatus(friend.getUserId());
                    friendVO.setStatus(BooleanUtils.isTrue(onlineStatus) ? 1 : 0);
                    return friendVO;
                })
                .collect(Collectors.toList());

        // 构建分组集合
        ResponseResult groupResult = this.friendGroupService.getUserAllGroup(userId);
        if (BooleanUtils.isFalse(groupResult.isSuccess())) {
            return ResponseResult.FAIL("查询好友信息失败");
        }
        List<FriendGroup> groupList = (List<FriendGroup>) groupResult.getData();

        // 响应数据
        return ResponseResult.SUCCESS(new FriendListResponse()
                .setFriendList(friendVOList).setGroupList(groupList));
    }


    /**
     * 获取用户的黑名单列表
     *
     * @param userId
     *
     * @return
     */
    @Override
    public ResponseResult getUserBlackList(Long userId) {
        List<Friend> blackerList = this.lambdaQuery()
                .eq(Friend::getUserId, userId)
                .eq(Friend::getRelation, FriendConstants.FriendRelationship.BLACK.ordinal())
                .list();
        // 判断黑名单是否有人
        if (CollectionUtils.isEmpty(blackerList)) {
            // 黑名单列表为空
            return ResponseResult.SUCCESS(Collections.emptyList());
        }

        // 黑名单不为空
        // 转换为 VO
        List<Long> blackIdList = blackerList.stream().map(Friend::getFriendId).toList();
        // 批量查询好友信息
        ResponseResult blackResult = this.userService.getUserAndInfoList(blackIdList);

        ResponseResult result = ResponseResult.SUCCESS(Collections.emptyList());
        if (BooleanUtils.isTrue(blackResult.isSuccess())) {
            List<UserVO> blackerVOList = (List<UserVO>) blackResult.getData();
            Map<Long, Friend> friendMap = blackerList.stream().collect(Collectors.toMap(Friend::getFriendId, value -> value));

            List<FriendVO> list = blackerVOList.stream().map(
                            blackerVO -> FriendMapStructure.INSTANCE.toVO(friendMap.get(blackerVO.getId()), blackerVO))
                    .sorted(Comparator.comparingInt(FriendVO::getStatus)).toList();

            result = ResponseResult.SUCCESS(list);
        }

        return result;

    }


    /**
     * 修改好友信息
     *
     * @param request
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult updateFriendInfo(ModifyFriendInfoRequest request) {

        // 验证登录情况

        // 参数校验
        String ownerId = request.getUserId();
        String friendId = request.getFriendId();
        Integer op = request.getAction();

        if (StringUtils.isEmpty(ownerId) || StringUtils.isEmpty(friendId)) {
            // 主体缺失
            return ResponseResult.FAIL(ResultCode.BAD_REQUEST).setMessage("请选择正确的好友!");
        }

        Boolean update = null;
        // 判断操作类型
        ModifyFriendInfoRequest.Action action = ModifyFriendInfoRequest.Action.values()[op];
        if (Objects.equals(action, ModifyFriendInfoRequest.Action.remarkName)) {
            // 修改好友备注信息，备注名称
            update = this.lambdaUpdate()
                    .eq(Friend::getUserId, request.getUserId())
                    .eq(Friend::getFriendId, request.getFriendId())
                    .set(StringUtils.isNotBlank(request.getRemarkName()), Friend::getRemarkName, request.getRemarkName())
                    .set(StringUtils.isNotBlank(request.getRemarkDescription()), Friend::getRemarkInfo, request.getRemarkDescription())
                    .update();
        } else if (Objects.equals(action, ModifyFriendInfoRequest.Action.group)) {
            // 修改好友分组
            update = this.lambdaUpdate()
                    .eq(Friend::getUserId, request.getUserId())
                    .eq(Friend::getFriendId, request.getFriendId())
                    .set(StringUtils.isNotBlank(request.getGroup()), Friend::getGroup, request.getGroup())
                    .update();
        }

        if (BooleanUtils.isFalse(update)) {
            return ResponseResult.FAIL("更新好友此信息失败").setMessage("更新好友此信息失败");
        }

        // 更新成功
        return ResponseResult.SUCCESS("更新好友信息成功");
    }


    /**
     * 删除好友: 双向, 聊天记录，会话表等
     *
     * @param friendId
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult removeFriend(String friendId, User user) throws ApiException {

        // 参数校验
        if (Objects.isNull(user) || Objects.isNull(user.getId())) {
            return ResponseResult.FAIL("抱歉您还未登录或登录过期了!");
        }
        if (StringUtils.isEmpty(friendId)) {
            return ResponseResult.FAIL("请选择需要删除的好友!");
        }

        // 删除双向好友关系
        Integer relations = this.friendMapper.deleteByUserIdAndFriendId(user.getId(), Long.valueOf(friendId));
        // 如果 删除受影响的行数不是2，则说明删除过多，应该回滚
        if (relations != 2) {
            // 执行回滚
            throw new ApiException(ResultCode.SERVER_BUSY);
        }

        // 发布消息，事件：删除聊天记录:

        return ResponseResult.SUCCESS("删除好友成功!").setMessage("删除好友成功!");
    }


    /**
     * 修改好友状态
     *
     * @param request
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult updateFriendStatus(ModifyFriendStatusRequest request) {

        // 参数校验
        String friendId = request.getFriendId();
        Integer status = request.getStatus();
        String userId = request.getUserId();

        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(friendId)) {
            return ResponseResult.FAIL("请选择正确的好友!");
        }
        // 获取需要修改的状态
        FriendConstants.FriendRelationship relationship = FriendConstants.FriendRelationship.values()[status];
        // 更新关系
        boolean update = this.lambdaUpdate()
                .eq(Friend::getUserId, userId)
                .eq(Friend::getFriendId, friendId)
                .set(Friend::getRelation, relationship.ordinal())
                .update();
        if (BooleanUtils.isFalse(update)) {
            return ResponseResult.FAIL("修改好友关系失败!").setResultCode(ResultCode.SERVER_BUSY);
        }

        return ResponseResult.SUCCESS(update).setMessage("修改成功!");
    }


    /**
     * 获取用户指定分组好友
     *
     * @param userId
     * @param groupId
     *
     * @return
     */
    @Override
    public ResponseResult getUserFriendByGroup(Long userId, Long groupId) {

        // 参数校验
        if (Objects.isNull(userId) || Objects.isNull(groupId)) {
            return ResponseResult.FAIL();
        }

        // 获取好友
        List<Friend> list = this.lambdaQuery()
                .eq(Friend::getUserId, userId)
                .eq(Friend::getGroupId, groupId)
                .list();
        if (CollectionUtils.isEmpty(list)) {
            list = Collections.emptyList();
        }
        // 响应
        return ResponseResult.SUCCESS(list);
    }


    /**
     * 获取指定好友VO
     *
     * @param userId
     * @param friendId
     *
     * @return
     */
    @Override
    public FriendVO getUserFriendById(Long userId, Long friendId) {

        Friend friend = this.friendMapper.selectFriendByTowUser(userId, friendId);
        // TODO 获取VO 信息
        // 1. 获取在线状态
        Boolean userOnlineStatus = this.authorizationService.getUserOnlineStatus(friendId);
        // 2.转换
        FriendVO friendVO = FriendMapStructure.INSTANCE.toVO(friend, null);
        // 3.设置在线状态
        friendVO.setOnline(userOnlineStatus);

        return friendVO;
    }


    /**
     * 检索出用户可以邀请的全部好友
     *
     * @param userId
     *
     * @return
     */
    @Override
    public List<Friend> getAllEnableInviteFriends(String groupId, String userId) {

        // 1. 检索所有好友
        List<Friend> friendList = this.friendMapper.selectAllFriends(userId);

        // 2. 好友中没有加入群聊的
        List<Friend> enableInviteFriendList = friendList.stream()
                .filter(friend -> {
                    boolean isMember = this.groupMemberService.isGroupMember(Long.valueOf(groupId), friend.getFriendId());
                    // 不是群员，添加进入可邀请列表
                    return !isMember;
                }).sorted(Comparator.comparing(Friend::getTop).reversed()
                        .thenComparing(Friend::getUpdateTime).reversed()
                        .thenComparing(Friend::getRemarkName, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(Friend::getNickname, String.CASE_INSENSITIVE_ORDER))
                .toList();

        return enableInviteFriendList;
    }


    /**
     * 获取用户指定好友
     *
     * @param senderId
     * @param receiverId
     *
     * @return
     */
    @Override
    public Friend getFriendById(Long senderId, Long receiverId) {
        Friend friend = this.friendMapper.selectFriendByTowUser(senderId, receiverId);
        return friend;
    }


    /**
     * 获取好友 vo 信息
     *
     * @param id
     *
     * @return
     */
    @Override
    public FriendVO getFriendVOById(Long id) {

        Friend friend = this.getById(id);
        // 获取 userVo 信息
        UserVO userVO = this.userService.getUserVO(friend.getUserId());
        FriendVO friendVO = FriendMapStructure.INSTANCE.toVO(friend, userVO);

        return friendVO;
    }


    /**
     * 通过关键字查询好友列表
     *
     * @param userId
     * @param keyword
     *
     * @return
     */
    @Override
    public List<FriendVO> getUserFriendListByKeyword(Long userId, String keyword) {

        List<Friend> friendList = this.friendMapper.selectFriendByKeyword(userId, keyword);

        // 查询 friend 的 vo 信息
        List<FriendVO> friendVOList = friendList.stream().map(friend -> this.getFriendVOById(friend.getId())).toList();

        return friendVOList;
    }


    /**
     * 获取用户好友分组列表
     *
     * @return
     */
    @Override
    public ResponseResult getUserFriendGroupList() {

        Long userId = UserContextHolder.get().getId();
        return this.friendGroupService.getUserAllGroup(userId);
    }


    /**
     * 修改好友分组信息
     * @param request
     * @return
     */
    @Override
    public ResponseResult modifyFriendGroup(ModifyFriendGroupRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || Objects.isNull(request.getGroupId()) || Objects.isNull(request.getFriendId());
        if (BooleanUtils.isTrue(preCheck)) {
            // 校验失败
            return ResponseResult.FAIL();
        }

        // 修改分组
        Long friendId = request.getFriendId();
        Long groupId = request.getGroupId();

        // 查看好友
        Long userId = UserContextHolder.get().getId();
        Friend friend = this.getFriendById(userId, friendId);
        if (Objects.isNull(friend)) {
            // 好友不存在
            return ResponseResult.FAIL().setMessage("好友不存在");
        }

        // 查看分组
        FriendGroup oldGroup = this.friendGroupService.getById(friend.getGroupId());
        FriendGroup newGroup = this.friendGroupService.getById(groupId);

        if (Objects.isNull(newGroup)) {
            // 分组不存在
            return ResponseResult.FAIL().setMessage("分组不存在");
        }

        if (BooleanUtils.isFalse(Objects.equals(groupId, oldGroup.getId()))) {
            // 更新分组
            friend.setGroup(newGroup.getName())
                    .setGroupId(groupId);
            boolean updateFriend = this.updateById(friend);
            // 更新分组人数
            // 4.3 更新好友old分组数量
            boolean oldCountUpdate = this.friendGroupService.updateById(oldGroup.setCount(oldGroup.getCount() - 1));
            // 4.4 更新new分组好友数量
            boolean newCountUpdate = this.friendGroupService.updateById(newGroup.setCount(newGroup.getCount() + 1));
        }

        return ResponseResult.SUCCESS();
    }


}





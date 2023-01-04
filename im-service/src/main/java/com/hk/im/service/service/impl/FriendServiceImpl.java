package com.hk.im.service.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.constant.FriendConstants;
import com.hk.im.domain.constant.UserConstants;
import com.hk.im.domain.entity.Friend;
import com.hk.im.domain.entity.User;
import com.hk.im.domain.vo.FriendVO;
import com.hk.im.domain.vo.UserVO;
import com.hk.im.infrastructure.mapper.FriendMapper;
import com.hk.im.infrastructure.mapstruct.FriendMapStructure;
import com.hk.im.infrastructure.mapstruct.UserMapStructure;
import com.hk.im.service.service.FriendService;
import com.hk.im.service.service.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName : FriendServiceImpl
 * @author : HK意境
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

    /**
     * 是否好友关系
     * @param fromUserId
     * @param toUserId
     * @return Friend
     */
    @Override
    public Friend isFriendRelationship(Long fromUserId, Long toUserId) {

        Friend friend = this.friendMapper.selectFriendByTowUser(fromUserId, toUserId);
        return friend;
    }


    /**
     * 是否黑名单
     * @param fromUserId
     * @param toUserId
     * @return
     */
    @Override
    public Friend isFriendBlackList(Long fromUserId, Long toUserId) {
        return this.haveFriendRelationship(fromUserId, toUserId, FriendConstants.FriendRelationship.BLACK);
    }

    /**
     * 是否具有某种关系
     * @param fromUserId
     * @param toUserId
     * @param relation
     * @return
     */
    @Override
    public Friend haveFriendRelationship(Long fromUserId, Long toUserId, FriendConstants.FriendRelationship relation) {
        return this.friendMapper.selectTheRelationship(fromUserId, toUserId, relation.ordinal());
    }


    /**
     * 获取好友列表：注意分组，排序规则, 备注等
     * @param userId
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
     * 获取用户的黑名单列表
     * @param userId
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


}





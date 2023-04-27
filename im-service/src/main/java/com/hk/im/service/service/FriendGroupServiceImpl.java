package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.FriendGroupService;
import com.hk.im.client.service.FriendService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.entity.Friend;
import com.hk.im.domain.entity.FriendGroup;
import com.hk.im.domain.request.FriendGroupRequest;
import com.hk.im.domain.request.friend.EditFriendGroupListRequest;
import com.hk.im.infrastructure.mapper.FriendGroupMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName : FriendGroupServiceImpl
 * @author : HK意境
 * @date : 2023/2/8 21:31
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class FriendGroupServiceImpl extends ServiceImpl<FriendGroupMapper, FriendGroup> implements FriendGroupService {

    @Resource
    private FriendGroupMapper friendGroupMapper;
    @Resource
    private FriendService friendService;

    /**
     * 获取用户默认分组
     * @param userId
     * @return
     */
    @Override
    public ResponseResult getUserDefaultGroup(Long userId) {
        FriendGroup defaultGroup = this.lambdaQuery().eq(FriendGroup::getUserId, userId)
                .eq(FriendGroup::getName,"默认分组")
                .one();
        if (Objects.isNull(defaultGroup)) {
            // 默认分组不存在，创建一个
            defaultGroup = new FriendGroup().setUserId(userId).setName("默认分组");
            this.save(defaultGroup);
        }
        return ResponseResult.SUCCESS(defaultGroup);
    }

    @Override
    public ResponseResult getUserAllGroup(Long userId) {
        List<FriendGroup> list = this.lambdaQuery().eq(FriendGroup::getUserId, userId).list();
        if (CollectionUtils.isEmpty(list)) {
            // 分组不存在，创建默认分组集合
            FriendGroup defaultGroup = new FriendGroup().setUserId(userId).setName("默认分组");
            FriendGroup blackListGroup = new FriendGroup().setUserId(userId).setName("黑名单");
            FriendGroup carefulGroup = new FriendGroup().setUserId(userId).setName("特别关心");
            // 创建
            friendGroupMapper.insert(defaultGroup);
            friendGroupMapper.insert(blackListGroup);
            friendGroupMapper.insert(carefulGroup);
            // 设置
            list.add(defaultGroup);
            list.add(blackListGroup);
            list.add(carefulGroup);
        }
        return ResponseResult.SUCCESS(list);
    }


    /**
     * 创建分组
     * @param request
     * @return
     */
    @Override
    public ResponseResult createFriendGroup(FriendGroupRequest request) {
        // 参数校验
        boolean preCheck = Objects.isNull(request) || StringUtils.isEmpty(request.getGroupName());
        if (BooleanUtils.isTrue(preCheck)) {
            // 前置校验失败
            return ResponseResult.FAIL("请求参数不完整!");
        }

        String userId = request.getUserId();
        if (StringUtils.isEmpty(userId)) {
            return ResponseResult.FAIL("抱歉您还未登录!");
        }

        // 校验操作
        if (request.getOperation() != 1) {
            return ResponseResult.FAIL("该操作暂不不支持!");
        }

        // 创建分组
        FriendGroup group = this.friendGroupMapper.getTheFriendGroup(Long.valueOf(userId), request.getGroupName());
        if (Objects.nonNull(group)) {
            // 分组存在
            return ResponseResult.SUCCESS("分组已经存在");
        }

        // 创建分组
        boolean save = this.save(new FriendGroup()
                .setUserId(Long.valueOf(userId)).setName(request.getGroupName()));
        if (BooleanUtils.isFalse(save)) {
            // 跟新失败
            return ResponseResult.FAIL("创建分组失败!");
        }

        return ResponseResult.SUCCESS("创建分组成功!");
    }

    /**
     * 删除分组
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteFriendGroup(FriendGroupRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || StringUtils.isEmpty(request.getGroupName());
        if (BooleanUtils.isTrue(preCheck)) {
            // 前置校验失败
            return ResponseResult.FAIL("请求参数不完整!");
        }

        String userId = request.getUserId();
        if (StringUtils.isEmpty(userId)) {
            return ResponseResult.FAIL("抱歉您还未登录!");
        }

        // 校验操作
        if (request.getOperation() != 2) {
            return ResponseResult.FAIL("该操作暂不不支持!");
        }

        // 删除分组
        FriendGroup theFriendGroup = this.friendGroupMapper.getTheFriendGroup(Long.valueOf(userId), request.getGroupName());
        if (Objects.isNull(theFriendGroup)) {
            // 分组不存在
            return ResponseResult.FAIL("删除分组失败,不存在该分组!");
        }
        Boolean delete = this.friendGroupMapper.deleteUserGroup(Long.valueOf(userId), request.getGroupName());
        if (BooleanUtils.isFalse(delete)) {
            // 失败
            return ResponseResult.FAIL("删除分组失败!");
        }

        // 移动分组用户到默认分组
        FriendGroup userDefaultGroup = this.friendGroupMapper.getUserDefaultGroup(Long.valueOf(userId));
        ResponseResult result = this.friendService.getUserFriendByGroup(Long.valueOf(userId), theFriendGroup.getId());
        if (BooleanUtils.isFalse(result.isSuccess())) {
            // 获取好友列表失败
            return ResponseResult.FAIL("删除分组失败!");
        }

        // 更新分组
        boolean update = this.friendService.lambdaUpdate()
                .set(Friend::getGroup, "默认分组")
                .set(Friend::getGroupId, userDefaultGroup.getId())
                .eq(Friend::getUserId, Long.valueOf(userId))
                .eq(Friend::getGroupId, theFriendGroup.getId())
                .update();
        if (BooleanUtils.isFalse(update)) {
            // 失败
            return ResponseResult.FAIL("删除分组失败!");
        }

        // 更新默认分组好友数量
        List<Friend> friendList = (List<Friend>) result.getData();
        Boolean increase = this.friendGroupMapper.increaseByCountUserGroup(Long.valueOf(userId), "默认分组", friendList.size());
        if (BooleanUtils.isFalse(increase)) {
            // 失败
            return ResponseResult.FAIL("删除分组失败!");
        }

        return ResponseResult.SUCCESS("删除分组成功!");
    }

    /**
     * 跟新分组
     * @param request
     * @return
     */
    @Override
    public ResponseResult renameFriendGroup(FriendGroupRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || StringUtils.isEmpty(request.getGroupName()) || StringUtils.isEmpty(request.getNewName());
        if (BooleanUtils.isTrue(preCheck)) {
            // 前置校验失败
            return ResponseResult.FAIL("请求参数不完整!");
        }

        String userId = request.getUserId();
        if (StringUtils.isEmpty(userId)) {
            return ResponseResult.FAIL("抱歉您还未登录!");
        }

        // 校验操作
        if (request.getOperation() != 3) {
            return ResponseResult.FAIL("该操作暂不不支持!");
        }

        // 跟新分组名称
        boolean update = this.lambdaUpdate()
                .set(StringUtils.isNotEmpty(request.getNewName()), FriendGroup::getName, request.getNewName())
                .eq(FriendGroup::getUserId, Long.valueOf(userId))
                .eq(FriendGroup::getName,request.getGroupName())
                .update();

        if (BooleanUtils.isFalse(update)) {
            // 跟新失败
            return ResponseResult.FAIL("更新分组名称失败!");
        }

        return ResponseResult.SUCCESS("更新分组名称成功");
    }


    /**
     * 移动分组
     * @param request
     * @return
     */
    @Override
    public ResponseResult moveFriendGroup(FriendGroupRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || StringUtils.isEmpty(request.getGroupName()) || StringUtils.isEmpty(request.getNewName());
        if (BooleanUtils.isTrue(preCheck)) {
            // 前置校验失败
            return ResponseResult.FAIL("请求参数不完整!");
        }

        String userId = request.getUserId();
        if (StringUtils.isEmpty(userId)) {
            return ResponseResult.FAIL("抱歉您还未登录!");
        }

        // 校验操作
        if (request.getOperation() != 4) {
            return ResponseResult.FAIL("该操作暂不不支持!");
        }

        // 分组列表
        List<String> friendIdList = request.getFriendList();
        if (CollectionUtils.isEmpty(friendIdList)) {
            return ResponseResult.SUCCESS("移动分组成功(待移动好友为0)");
        }

        // 移动分组：修改好友列表好友的分组为指定新的分组

        // 1.查询两个分组
        String oldGroupName = request.getGroupName();
        String newGroupName = request.getNewName();
        FriendGroup oldGroup = this.friendGroupMapper.getTheFriendGroup(Long.valueOf(userId), oldGroupName);
        FriendGroup newGroup = this.friendGroupMapper.getTheFriendGroup(Long.valueOf(userId), oldGroupName);
        // 2.分组是否存在
        if (Objects.isNull(oldGroup) || Objects.isNull(newGroup)) {
            // 分组不存在
            return ResponseResult.FAIL("分组不存在!");
        }
        // 3.是否同一分组
        if (StringUtils.equals(oldGroupName,newGroupName)) {
            // 两个分组名称相同
            return ResponseResult.SUCCESS("移动分组成功!");
        }

        // 4.不同分组，进行移动
        // 4.1 检查待移动分组好友是否都属于该分组
        List<Friend> friendList = this.friendService.listByIds(friendIdList);
        for (Friend friend : friendList) {
            // 是否是old 分组
            Long groupId = friend.getGroupId();
            if (!groupId.equals(oldGroup.getId())) {
                // 该好友分组不是old 分组
                return ResponseResult.FAIL("移动分组失败!好友不属于该分组!");
            }
            friend.setGroup(newGroupName);
            friend.setGroupId(newGroup.getId());
        }

        // 4.2 更新好友分组
        boolean updateFriend = this.friendService.updateBatchById(friendList);
        if (BooleanUtils.isFalse(updateFriend)) {
            // 跟新好友分组失败
            return ResponseResult.FAIL("移动分组失败!");
        }

        // 4.3 更新好友old分组数量
        boolean oldCountUpdate = this.updateById(oldGroup.setCount(Math.max(0, oldGroup.getCount() - friendList.size())));
        // 4.4 更新new分组好友数量
        boolean newCountUpdate = this.updateById(newGroup.setCount(newGroup.getCount() + friendList.size()));

        if (BooleanUtils.isFalse(oldCountUpdate) || BooleanUtils.isFalse(newCountUpdate)) {
            // 获取分组用户失败
            return ResponseResult.FAIL("移动分组失败!");
        }

        return ResponseResult.SUCCESS("移动分组成功!");
    }


    /**
     * 编辑好友分组列表
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult editFriendGroupList(EditFriendGroupListRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || Objects.isNull(request.getFriendGroupList());
        if (BooleanUtils.isTrue(preCheck)) {
            // 参数校验失败
            return ResponseResult.FAIL();
        }

        // 新的分组列表存在，但是可能为空集
        List<FriendGroup> newGroupList = request.getFriendGroupList();

        // 查询现在的好友分组
        Long userId = request.getUserId();
        if (Objects.isNull(userId)) {
            userId = UserContextHolder.get().getId();
        }
        List<FriendGroup> oldGroupList = this.lambdaQuery().eq(FriendGroup::getUserId, userId).list();

        // 对传入的分组先进性创建操作：
        List<FriendGroup> waitCreateGroupList = newGroupList.stream().filter(group -> group.getId() == 0).toList();

        // 创建分组
        for (FriendGroup group : waitCreateGroupList) {
            FriendGroup friendGroup = new FriendGroup();
            friendGroup.setName(group.getName())
                            .setUserId(userId).setCount(0);
            boolean save = this.save(friendGroup);
        }

        // 对比传入的分组进行更新操作：
        List<FriendGroup> waitUpdateGroupList = newGroupList.stream().filter(group -> group.getId() != 0).toList();
        for (FriendGroup group : waitUpdateGroupList) {
            this.updateById(group);
        }

        // TODO 对比传入的分组进行删除操作：

        return ResponseResult.SUCCESS();
    }


}





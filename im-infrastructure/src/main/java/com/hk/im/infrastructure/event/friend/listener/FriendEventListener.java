package com.hk.im.infrastructure.event.friend.listener;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.hk.im.domain.constant.FriendConstants;
import com.hk.im.domain.entity.FriendApply;
import com.hk.im.domain.entity.FriendGroup;
import com.hk.im.domain.request.ApplyHandleRequest;
import com.hk.im.infrastructure.event.friend.event.AddFriendSuccessEvent;
import com.hk.im.infrastructure.event.friend.event.ApplyHandleEvent;
import com.hk.im.infrastructure.event.friend.event.FriendApplyEvent;
import com.hk.im.infrastructure.mapper.FriendApplyMapper;
import com.hk.im.infrastructure.mapper.FriendGroupMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : FriendEventListener
 * @date : 2023/1/2 19:44
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class FriendEventListener {

    @Resource
    private FriendApplyMapper friendApplyMapper;
    @Resource
    private FriendGroupMapper friendGroupMapper;

    /**
     * 添加好友事件
     * @param event
     */
    @Async
    @EventListener
    public void friendApplyHandler(FriendApplyEvent event) {
        // 写通知
        log.info("好友申请事件：{}", event.getData());

    }


    /**
     * 好友申请处理事件
     * @param event
     */
    @Async
    @EventListener
    public void friendApplyHandler(ApplyHandleEvent event) {
        log.info("好友申请处理事件：{}", event.getData());

        ApplyHandleRequest request = event.getData();
        String applyId = request.getApplyId();
        FriendApply apply = friendApplyMapper.selectById(applyId);
        Long senderId = apply.getSenderId();
        Long acceptorId = apply.getAcceptorId();
        // 看接收者的申请操作
        FriendConstants.FriendApplyStatus operation =
                FriendConstants.FriendApplyStatus.values()[request.getOperation()];

        // 判断操作
        if (FriendConstants.FriendApplyStatus.REJECT.equals(operation)) {
            // 拒绝申请: 发布事件，响应请求

        } else if (FriendConstants.FriendApplyStatus.IGNORE.equals(operation)) {
            // 拒绝申请: 发布事件，响应请求

        } else if (FriendConstants.FriendApplyStatus.AGREE.equals(operation)) {
            // 同意添加为好友
            // 删除申请信息
            LambdaUpdateChainWrapper<FriendApply> wrapper = new LambdaUpdateChainWrapper<>(this.friendApplyMapper);
            boolean remove = wrapper.eq(FriendApply::getSenderId, senderId)
                    .eq(FriendApply::getAcceptorId, acceptorId)
                    .remove();
        }

    }


    /**
     * 成为好友
     * @param event
     */
    @Async
    @EventListener
    public void addFriendSuccessEventHandler(AddFriendSuccessEvent event) {

        FriendApply friendApply = event.getData();
        // 默认分组添加好友数量
        this.friendGroupMapper.increaseUserGroupCount(friendApply.getSenderId(),"默认分组");
        this.friendGroupMapper.increaseUserGroupCount(friendApply.getAcceptorId(),"默认分组");
    }


}

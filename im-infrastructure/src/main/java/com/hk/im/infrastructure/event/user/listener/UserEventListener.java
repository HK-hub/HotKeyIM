package com.hk.im.infrastructure.event.user.listener;

import com.hk.im.domain.entity.FriendGroup;
import com.hk.im.domain.entity.User;
import com.hk.im.domain.entity.UserInfo;
import com.hk.im.infrastructure.event.user.event.UserRegisterEvent;
import com.hk.im.infrastructure.event.user.event.UserUpdatedEvent;
import com.hk.im.infrastructure.mapper.FriendGroupMapper;
import com.hk.im.infrastructure.mapper.UserInfoMapper;
import com.hk.im.infrastructure.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : UserEventListener
 * @date : 2023/1/1 20:21
 * @description : 监听用户事件
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class UserEventListener {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private FriendGroupMapper friendGroupMapper;

    /**
     * 用户更新事件
     *
     * @param event
     */
    @Async
    @EventListener
    public void userUpdatedEventHandler(UserUpdatedEvent event) {

        // 更新用户信息
        this.userMapper.updateById(event.getData());
    }


    /**
     * 用户注册事件
     *
     * @param event
     */
    @Async
    @EventListener
    public void userRegisterEventHandler(UserRegisterEvent event) {

        User user = event.getData();
        // 设置两个好友分组：默认分组，黑名单
        FriendGroup blackListGroup = new FriendGroup().setUserId(user.getId()).setName("黑名单");
        FriendGroup carefulGroup = new FriendGroup().setUserId(user.getId()).setName("特别关心");
        FriendGroup defaultGroup = new FriendGroup().setUserId(user.getId()).setName("默认分组");
        friendGroupMapper.insert(defaultGroup);
        friendGroupMapper.insert(blackListGroup);
        friendGroupMapper.insert(carefulGroup);

        // 设置默认签名：你好，我是XXX
        UserInfo userInfo = this.userInfoMapper.getUserInfoByUserId(user.getId());
        String signature = "你好,我是" + user.getUsername() + ",请多指教";
        this.userInfoMapper.updateById(userInfo.setSignature(signature));

    }


}

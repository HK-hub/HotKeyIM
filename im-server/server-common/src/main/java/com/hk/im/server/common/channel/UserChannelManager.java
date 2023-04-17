package com.hk.im.server.common.channel;

import com.alibaba.fastjson2.JSON;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.message.WebSocketMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author : HK意境
 * @ClassName : UserChannelRelation
 * @date : 2023/1/5 17:53
 * @description : 用户-channel 映射关系
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class UserChannelManager {

    // 保存用户与其对应的channel: 使用 <set>集合是为了支持多端登录, 如果为群聊，<set> 集合则表示群员的 Channel
    public static final Map<Long, Set<Channel>> userChannelMap = new ConcurrentHashMap<>(1024);
    public static ChannelGroup clientChannelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    // 群聊在线成员id集合
    public static Map<Long, Set<Long>> groupMemberMap = new ConcurrentHashMap<>(1024);

    // 并发加锁
    private static final Lock lock = new ReentrantLock();

    /**
     * 添加用户channel
     * @param userId
     * @param channel
     * @return
     */
    public static void add(@NonNull Long userId, @NonNull Channel channel) {
        lock.lock();
        try{
            Set<Channel> channels = userChannelMap.get(userId);
            if (ObjectUtils.isEmpty(channels) || channels.size() == 0) {
                channels = new HashSet<>();
            }
            channels.add(channel);
            userChannelMap.put(userId, channels);
            clientChannelGroup.add(channel);

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }


    /**
     * 添加用户  Channel 到 group 群聊中：<groupId, <群员Channel>>
     * @param groupId
     * @param channel
     */
    public static void addGroupChannel(@NonNull Long groupId, @NonNull Long userId, @NonNull Channel channel) {

        lock.lock();
        try{
            Set<Channel> channels = userChannelMap.get(groupId);
            if (ObjectUtils.isEmpty(channels) || channels.size() == 0) {
                channels = new HashSet<>();
            }
            channels.add(channel);
            userChannelMap.put(groupId, channels);
            clientChannelGroup.add(channel);

            Set<Long> memberSet = groupMemberMap.get(groupId);
            if (CollectionUtils.isEmpty(memberSet)) {
                // 群聊成员id 集合不存在
                memberSet = new HashSet<>();
            }
            memberSet.add(userId);
            groupMemberMap.put(groupId, memberSet);

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }


    /**
     * 移除
     * @param channel
     */
    public static void remove(@NonNull Channel channel) {
        userChannelMap.entrySet().stream().filter(entry -> entry.getValue().contains(channel))
                .forEach(entry -> entry.getValue().remove(channel));
        clientChannelGroup.remove(channel);
    }

    public static void clearAll() {
        userChannelMap.clear();
        clientChannelGroup.clear();
    }


    /**
     * Get channel by uid
     * @param userId     uid
     * @return channel
     */
    @Nullable
    public static Set<Channel> getUserChannel(@NonNull Long userId) {
        return userChannelMap.get(userId);
    }


    /**
     * 获取群聊成员Channel
     * @param groupId
     * @return
     */
    @Nullable
    public static Set<Channel> getGroupChannel(@NonNull Long groupId) {
        Set<Channel> channelSet = userChannelMap.get(groupId);
        if (CollectionUtils.isEmpty(channelSet)) {
            channelSet = new HashSet<>();
        }
        return channelSet;
    }


    /**
     * 获取群聊成员Channel，排除我
     * @param groupId
     * @return
     */
    public static Set<Channel> getGroupChannelExcludeMe(@NonNull Long groupId, Long userId) {
        Set<Channel> channelSet = userChannelMap.get(groupId);
        if (CollectionUtils.isEmpty(channelSet)) {
            channelSet = new HashSet<>();
        }
        // 获取我的channel
        Set<Channel> me = getUserChannel(userId);
        channelSet.removeAll(me);
        return channelSet;
    }

    /**
     * 获取群聊成员id 集合
     * @param groupId
     * @return
     */
    public static Set<Long> getGroupMemberIdSet(@NonNull Long groupId) {
        return groupMemberMap.get(groupId);
    }


    public static Map<Long, Set<Channel>> getUserChannelMap() {
        return userChannelMap;
    }

    /**
     * 向指定 user 写入消息
     * @param uid
     * @param message
     * @param typeEnum
     * @throws
     */
    public static void writeAndFlush(@NonNull Long uid, @NonNull Object message, @NonNull MessageConstants.MessageActionType typeEnum) {
        Set<Channel> channelSet = userChannelMap.get(uid);
        if (CollectionUtils.isEmpty(channelSet) || channelSet.size() == 0) {
            return;
        }
        // 发送消息
        for (Channel channel : channelSet) {
            // 通道还在线
            if (channel.isActive()) {
                // 构造消息
                WebSocketMessage webSocketMessage = new WebSocketMessage()
                        .setMessageActionType(typeEnum.ordinal())
                        .setMessageData(JSON.toJSONString(message));
                String jsonString = JSON.toJSONString(webSocketMessage);
                TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(jsonString);
                // 发送消息
                ChannelFuture channelFuture = channel.writeAndFlush(textWebSocketFrame);
                channelFuture.addListener((ChannelFutureListener) future -> {
                    log.debug("对uid：{}, 发送websocket消息：{}", uid, jsonString);
                });
            }
        }
    }

    public static void writeAndFlush(@NonNull Long uid, @NonNull Object message) {
        Set<Channel> channelSet = userChannelMap.get(uid);
        if (CollectionUtils.isEmpty(channelSet) || channelSet.size() == 0) {
            return;
        }
        // 发送消息
        for (Channel channel : channelSet) {
            // 通道还在线
            if (channel.isActive()) {
                // 构造消息
                String jsonString = JSON.toJSONString(message);
                TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(jsonString);
                // 发送消息
                ChannelFuture channelFuture = channel.writeAndFlush(textWebSocketFrame);
                channelFuture.addListener((ChannelFutureListener) future -> {
                    log.debug("对uid：{}, 发送websocket消息：{}", uid, jsonString);
                });
            }
        }
    }


    /**
     * 向所属有用户发送消息，一般用于发送控制消息
     * @param message
     * @param typeEnum
     */
    public static void writeAndFlush(@NonNull Object message, @NonNull MessageConstants.MessageActionType typeEnum) {
        // 构造消息
        WebSocketMessage webSocketMessage = new WebSocketMessage()
                .setMessageActionType(typeEnum.ordinal())
                .setMessageData(JSON.toJSONString(message));
        String jsonString = JSON.toJSONString(webSocketMessage);
        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(jsonString);
        // 发送消息
        userChannelMap.forEach((uid, channels) -> {
            for (Channel channel : channels) {
                if (channel.isActive()) {
                    ChannelFuture channelFuture = channel.writeAndFlush(textWebSocketFrame);
                    channelFuture.addListener((ChannelFutureListener)future -> {
                        log.debug("对uid：{}, 发送websocket消息：{}", uid, jsonString);
                    });
                }
            }
        });
    }


    /**
     * 向置顶群聊群员写消息
     * @param groupId
     * @param memberId
     * @param message
     */
    public static void writeGroupMemberAndFlush(Long groupId, Long memberId, Object message) {


    }

}

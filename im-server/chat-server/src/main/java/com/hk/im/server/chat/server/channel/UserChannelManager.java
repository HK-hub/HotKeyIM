package com.hk.im.server.chat.server.channel;

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
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.lang.Nullable;

import java.util.HashSet;
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

    // 保存用户与其对应的channel: 使用 <set>集合是为了支持多端登录
    public static final Map<Long, Set<Channel>> userChannelMap = new ConcurrentHashMap<>();
    public static ChannelGroup clientChannelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
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
     * @param uid     uid
     * @return channel
     */
    @Nullable
    public static Set<Channel> getUserChannel(@NonNull Long uid) {
        return userChannelMap.get(uid);
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
        if (ObjectUtils.isEmpty(channelSet) || channelSet.size() == 0) {
            return;
        }
        // 发送消息
        for (Channel channel : channelSet) {
            // 通道还在线
            if (channel.isActive()) {
                // 构造消息
                WebSocketMessage webSocketMessage = new WebSocketMessage()
                        .setMessageType(typeEnum.ordinal())
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


    /**
     * 向所属有用户发送消息，一般用于发送控制消息
     * @param message
     * @param typeEnum
     */
    public static void writeAndFlush(@NonNull Object message, @NonNull MessageConstants.MessageActionType typeEnum) {
        // 构造消息
        WebSocketMessage webSocketMessage = new WebSocketMessage()
                .setMessageType(typeEnum.ordinal())
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



}

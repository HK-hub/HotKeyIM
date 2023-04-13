package com.hk.im.server.signal.client;

import io.netty.channel.Channel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : PeerClient
 * @date : 2023/3/20 15:28
 * @description : 用户客户端，特指加入某一个房间的连接用户
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class PeerClient {

    // 当前连接用户id
    private Long userId;

    // websocket 连接通道
    private Channel connectChannel;

    // 用户连接的房间id
    private Long roomId;


    /**
     * 用户加入房间进行通话
     * @param userId 加入用户
     * @param connectChannel 用户channel
     * @param roomId 加入房间id
     */
    public PeerClient(Long userId, Channel connectChannel, Long roomId) {
        this.userId = userId;
        this.connectChannel = connectChannel;
        this.roomId = roomId;
    }
}

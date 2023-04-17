package com.hk.im.server.common.message;

import lombok.Data;

/**
 * @ClassName : PongMessage
 * @author : HK意境
 * @date : 2023/4/17 21:24
 * @description : pong
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
public class PongMessage{
    private String event = "heartbeat";

    private String content = "pong";
}

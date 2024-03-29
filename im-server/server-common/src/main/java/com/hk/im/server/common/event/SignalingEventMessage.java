package com.hk.im.server.common.event;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : SignalingEventMessage
 * @date : 2023/3/22 14:16
 * @description : 客户端发送的信令
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
@ToString
public class SignalingEventMessage {


    // 信令类型
    protected String cmd;

}

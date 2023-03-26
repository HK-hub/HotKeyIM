package com.hk.im.server.signal.cmd;

import com.hk.im.server.common.event.SignalingEventMessage;
import lombok.Data;
import lombok.ToString;

/**
 * @author : HK意境
 * @ClassName : JoinRoomEventMessage
 * @date : 2023/3/23 10:21
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@ToString(callSuper = true)
public class JoinRoomEventMessage extends SignalingEventMessage {

    private String roomNumber;

    private String userId;

}

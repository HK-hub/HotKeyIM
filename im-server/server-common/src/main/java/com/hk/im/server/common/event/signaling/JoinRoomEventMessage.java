package com.hk.im.server.common.event.signaling;

import com.hk.im.server.common.event.SignalingEventMessage;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
@ToString(callSuper = true)
public class JoinRoomEventMessage extends SignalingEventMessage {

    private String roomId;

    private String userId;

    private String receiverId;

    private Integer type;

}

package com.hk.im.server.signal.constant;

/**
 * @author : HK意境
 * @ClassName : SignalingConstants
 * @date : 2023/3/22 14:50
 * @description : 信令常量
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class SignalingConstants {

    // 信令
    public static final String SIGNALING_TYPE = "signaling-event";

    // 主动加入房间
    public static final String SIGNALING_TYPE_JOIN = "join-event";

    // 告知加入者对方是谁
    public static final String SIGNALING_TYPE_RESP_JOIN = "join-resp-event";

    // 主动离开房间
    public static final String SIGNALING_TYPE_LEAVE = "leave-event";

    // 有人加入房间，通知已经在房间内的人
    public static final String SIGNALING_TYPE_NEW_PEER = "new-peer-event";

    // 有人离开房间，通知房间内的人
    public static final String SIGNALING_TYPE_PEER_LEAVE = "leave-peer-event";

    // offer：发送offer给对端peer
    public static final String SIGNALING_TYPE_OFFER = "offer-event";

    // answer： 发送offer给对端peer
    public static final String SIGNALING_TYPE_ANSWER = "answer-event";

    // 发送candidate给对端peer
    public static final String SIGNALING_TYPE_CANDIDATE = "candidate-event";

}

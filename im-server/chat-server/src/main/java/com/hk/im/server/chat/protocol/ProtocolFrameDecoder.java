package com.hk.im.server.chat.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author : HK意境
 * @ClassName : ProtocolFrameDecoder
 * @date : 2022/12/28 19:05
 * @description : 自定义协议的帧解码器
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class ProtocolFrameDecoder extends LengthFieldBasedFrameDecoder {

    // 要求正文长度如果全部发送文本，那么字数不超过 1024，
    private static final int DEFAULT_MAX_MESSAGE_SIZE = 2048;
    private static final int DEFAULT_LENGTH_FIELD_OFFSET = 12;
    private static final int DEFAULT_FIELD_LENGTH = 4;
    private static final int DEFAULT_ADJUSTMENT_LENGTH = 0;
    private static final int INITIAL_BYTES_TOSTRIP = 0;


    public ProtocolFrameDecoder() {
        this(DEFAULT_MAX_MESSAGE_SIZE, DEFAULT_LENGTH_FIELD_OFFSET, DEFAULT_FIELD_LENGTH, DEFAULT_ADJUSTMENT_LENGTH, INITIAL_BYTES_TOSTRIP);
    }

    private ProtocolFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}

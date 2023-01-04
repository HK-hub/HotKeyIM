package com.hk.im.server.chat.protocol;

import com.hk.im.server.chat.config.MetaDataConfig;

import java.nio.charset.StandardCharsets;

/**
 * @author : HK意境
 * @ClassName : ProtocolMetaData
 * @date : 2022/12/28 17:30
 * @description : 协议元数据
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class ProtocolMetaData {

    /**
     * 自定义协议：
     *      魔术：4字节：hkim 或者 HKIM
     *      版本：1字节：1
     *      序列化算法：1字节：protobuf
     *      指令类型: 1字节(支持127个)
     *      请求序号：4个字节
     *      正文长度：4个字节
     *      对其填充：1个字节
     *  -------------------------------16字节
     *      正文：n 字节
     */
    // 魔数
    public static final byte[] magic = "hkim".getBytes(StandardCharsets.UTF_8);
    // 版本
    public static final byte version = 1;
    // 序列化方式
    public static final byte serialization = (byte) MetaDataConfig.serializer.ordinal();
    // 对其填充
    public static final byte fillByte = (byte) 0xff;


}

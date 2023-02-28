package com.hk.im.server.chat.protocol;

import com.hk.im.server.chat.config.MetaDataConfig;
import com.hk.im.server.chat.serialization.SerializationEnum;
import com.hk.im.server.chat.serialization.SerializerFacade;
import com.hk.im.server.common.message.AbstractMessage;
import com.hk.im.server.common.message.MessageTypeDeterminer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author : HK意境
 * @ClassName : MessageCodecExecutor
 * @date : 2022/12/28 17:29
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class MessageCodecExecutor {

    public static void encode(ChannelHandlerContext channelHandlerContext, AbstractMessage message, ByteBuf out) throws Exception {
        // 1. 魔数，4字节
        out.writeBytes(ProtocolMetaData.magic);
        // 2. 版本，1 字节
        out.writeByte(ProtocolMetaData.version);
        // 3. 序列化算法，1字节
        out.writeByte(ProtocolMetaData.serialization);
        // 4. 指令类型：1字节
        out.writeByte(message.getMessageType());
        // 5. 请求序号：4个字节
        out.writeInt(message.getSequenceId());
        // 无意义，对齐填充
        out.writeByte(ProtocolMetaData.fillByte);
        // 7. 获取内容的字节数组
        SerializationEnum serializer = MetaDataConfig.serializer;
        byte[] content = SerializerFacade.serialize(serializer, message);
        // 6. 正文长度
        out.writeInt(content.length);
        // 8. 写真实内容
        out.writeBytes(content);
    }

    public static void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        // 读取魔数：4字节
        String magicNumber = in.readBytes(4).toString(StandardCharsets.UTF_8);
        // 读取版本：1字节
        byte version = in.readByte();
        // 读取序列化算法：1字节
        byte serialization = in.readByte();
        // 读取指令类型：1字节
        byte messageType = in.readByte();
        // 读取序列号
        int sequenceId = in.readInt();
        // 对其填充
        in.readByte();
        // 读取内容长度
        int contentLength = in.readInt();
        // 读取内容
        byte[] bytes = new byte[contentLength];
        in.readBytes(bytes, 0, contentLength);

        // 解析Message对象
        SerializationEnum serializer = SerializationEnum.getInstance(serialization);
        // 确定具体消息类型
        Class<?> clazz = MessageTypeDeterminer.getMessageClass(messageType);
        Object message = SerializerFacade.deserialize(serializer, clazz, bytes);

        log.debug("{},{},{},{},{},{}", magicNumber, version, serialization, messageType, sequenceId, contentLength);
        log.debug("message={}", message);
        // 放入输出列表，给后续handler 使用
        out.add(message);

    }


    /**
     * @ClassName : MessageCodecChecker
     * @author : HK意境
     * @date : 2022/12/28 1:19
     * @description : 编解码校验器: 校验ByteBuf 格式，内容数据
     * @Todo :
     * @Bug :
     * @Modified :
     * @Version : 1.0
     */
    private static class MessageCodecChecker {

        public static boolean checkMagicNumber() {


            return true;
        }

    }

}
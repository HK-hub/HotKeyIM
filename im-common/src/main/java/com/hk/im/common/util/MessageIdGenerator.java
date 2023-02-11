package com.hk.im.common.util;

import lombok.extern.slf4j.Slf4j;

/**
 * @author : HK意境
 * @ClassName : MessageIdGenerator
 * @date : 2023/2/10 20:32
 * @description : 消息ID 生成器
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Deprecated
public class MessageIdGenerator {


    // 最大自旋ID：2^12 = 4096
    private static final int MAX_MESSAGES_SEQ = 0xFFF;
    // 当前id
    private static int currentSeq = 0;


    /**
     * 融云消息ID生成算法：消息ID共占80bit: 时间戳(42bit)-自选ID(12bit)-会话类型(4bit)-会话ID(22bit)
     *
     * @param senderId    发送者id
     * @param receiverId  接收者id:[好友，群聊]
     * @param sessionId   会话id: 好友-好友会话id，群友-群聊会话id
     * @param sessionType 聊天类型：私聊，群聊，系统消息，客服，机器人等
     *
     * @return
     */
    @Deprecated
    public static String generateMessageIdOfRongCloud(Long senderId, Long receiverId, Long sessionId, Integer sessionType) {

        // 1.获取当前系统的时间戳，并赋值给消息 ID 的高 64 Bit
        long highBits = System.currentTimeMillis();

        // 2.获取一个自旋 ID ， highBits 左移 12 位，并将自旋 ID 拼接到低 12 位中
        int sequence = getMessageSeqOfRongCloud();
        highBits = highBits << 12;
        highBits = highBits | sequence;

        // 3.上步的 highBits 左移 4 位，将会话类型拼接到低 4 位
        highBits = highBits << 4;
        highBits = highBits | (sessionType & 0xF);

        // 4.取会话 ID 哈希值的低 22 位，记为 sessionIdInt
        int sessionIdInt = (int) (sessionId & 0x3FFFFF);

        // 5.highBits 左移 6 位，并将 sessionIdInt 的高 6 位拼接到 highBits 的低 6 位中：
        highBits = highBits << 6;
        highBits = highBits | (sessionIdInt >> 16);

        // 6.取会话 ID 的低 16 位作为 lowBits：
        int lowBits = (sessionIdInt & 0xFFFF) << 16;

        // 7.highBits 与 lowBits 拼接得到 80 Bit 的消息 ID，对其进行 32 进制编码，即可得到唯一消息 ID：
        StringBuilder sb = new StringBuilder()
                .append(highBits).append(lowBits);
        System.out.println(sb.toString());
        // 编码规则：从左至右，每 5 个 Bit 转换为一个整数，以这个整数作为下标，即可在下表中找到对应的字符。
        for (int i = 0; i < sb.length() / 5; i++) {
            String part = sb.substring(i * 5, (i + 1) * 5);
            System.out.println(i + " part=" + part);
        }

        return "";
    }


    /**
     * 获取下一个自旋id
     * 考虑：AtomicInteger 来进行优化
     *
     * @return
     */
    private synchronized static int getMessageSeqOfRongCloud() {
        int next = currentSeq++;
        if (next > MAX_MESSAGES_SEQ) {
            // 超过最大值，重置
            currentSeq = 0;
            next = currentSeq++;
        }
        return next;
    }


}

package com.hk.im.domain.constant;


/**
 * @author : HK意境
 * @ClassName : MessageConstantsTest
 * @date : 2023/3/10 9:47
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
class MessageConstantsTest {

    public static void main(String[] args) {


        MessageConstants.ChatMessageType[] values = MessageConstants.ChatMessageType.values();

        for (MessageConstants.ChatMessageType value : values) {
            System.out.println(value.name());
        }


    }



}
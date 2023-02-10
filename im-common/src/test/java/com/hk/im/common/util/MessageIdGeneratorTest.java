package com.hk.im.common.util;

import org.junit.jupiter.api.Test;


/**
 * @author : HK意境
 * @ClassName : MessageIdGeneratorTest
 * @date : 2023/2/10 21:10
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
class MessageIdGeneratorTest {

    private static final Long sessionId = 1612030998776700929L;


    @Test
    void generateMessageIdOfRongCloud() {

        String s = MessageIdGenerator.generateMessageIdOfRongCloud(sessionId, sessionId, sessionId, 1);

    }
}
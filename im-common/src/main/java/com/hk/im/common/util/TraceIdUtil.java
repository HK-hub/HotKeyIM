package com.hk.im.common.util;

import java.util.UUID;

/**
 * @author : HK意境
 * @ClassName : TraceIdUtil
 * @date : 2023/2/20 10:41
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class TraceIdUtil {

    public static String getTraceId() {

        return UUID.randomUUID().toString();

    }

}

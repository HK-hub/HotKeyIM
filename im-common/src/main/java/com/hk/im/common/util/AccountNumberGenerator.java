package com.hk.im.common.util;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author : HK意境
 * @ClassName : AccountNumberGenerator
 * @date : 2022/12/30 21:05
 * @description : 账号生产器
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class AccountNumberGenerator {

    private static final LongAdder adder = new LongAdder();

    // 9999 9999 99
    // - 固定时间戳
    // - redis 递增数据

    public static long nextAccount() {

        long account = System.currentTimeMillis() / 1000;
        return account;
    }


}

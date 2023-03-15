package com.hk.im.admin.config;

import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author : HK意境
 * @ClassName : ThreadPoolConfig
 * @date : 2023/3/14 15:48
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Configuration
public class ThreadPoolConfig {


    /**
     * 用于合并文件分片的线程池
     * IO 密集型
     * @return
     */
    @Bean(name = "mergeSliceFileThreadPool")
    public ThreadPoolExecutor mergeSliceFileThreadPool() {
        int core = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(core * 2, core * 2, 3, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10), new DefaultThreadFactory("mergeSliceFileThreadPool"));
    }


}

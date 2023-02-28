package com.hk.im.server.push.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author : HK意境
 * @ClassName : ExecutorConfiguration
 * @date : 2023/2/28 20:17
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Configuration
public class ExecutorConfiguration {

    public static final double blockRate = 0.5;

    /**
     * 推送消息模型为计算比例高于IO比例:
     * 线程数 = CPU 核心数 / (1 - 阻塞系数)
     * 阻塞系数=0.5
     * @return
     */
    @Bean
    public Executor asyncServiceExecutor() {
        log.info("start asyncServiceExecutor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // cpu 核心数
        int core = Runtime.getRuntime().availableProcessors();
        int threads = (int) (core / (1.0 - 0.5));
        //配置核心线程数
        executor.setCorePoolSize(threads);
        //配置最大线程数
        executor.setMaxPoolSize(threads);
        //配置队列大小
        executor.setQueueCapacity(99999);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("async-service-");

        //拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }

}

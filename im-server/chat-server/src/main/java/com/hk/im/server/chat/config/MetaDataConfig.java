package com.hk.im.server.chat.config;


import com.hk.im.server.chat.serialization.SerializationEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author : HK意境
 * @ClassName : MetaDataConfig
 * @date : 2022/12/28 22:26
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Data
@Component
public class MetaDataConfig {

    // 序列化算法
    public static SerializationEnum serializer = SerializationEnum.DEFAULT;
    // address
    public static String address = "localhost";
    // 端口
    public static int port = 9870;
    // path
    public static String path = "/channel";
    // 最大帧: 1MB
    public static long maxFrameSize = 10240;

    /**
     * 0 for automatic setting（The default is CPU * 2）
     */
    public static int workerThreads = 0;

    /**
     * 0 for automatic setting (The default is CPU * 2)
     */
    public static int bossThreads = 0;

    /**
     * Only in Linux environments can this be set to true
     */
    public static boolean epoll = false;

    // 读空闲时间: 2小时
    public static int READ_IDEL_TIME_OUT = 120;
    public static int WRITE_IDEL_TIME_OUT;
    public static int ALL_IDEL_TIME_OUT;
    public static TimeUnit timeUnit = TimeUnit.MINUTES;


    private static final Properties properties;
    static {
        try (InputStream in = MetaDataConfig.class.getResourceAsStream("/application.properties")) {
            properties = new Properties();
            properties.load(in);
            // 初始化
            init();
            // 检查参数
            check();
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * 初始化netty参数
     */
    public static void init() {
        // 序列化
        String sv = properties.getProperty("serializer.algorithm");
        System.out.println("序列化算法配置: " + sv);
        if (StringUtils.isNotEmpty(sv)) {
            serializer = SerializationEnum.valueOf(sv);
        }

        // 地址

        String av = properties.getProperty("netty.websocket.address");
        if (StringUtils.isNotEmpty(av)) {
            address = av;
        }

        // 端口
        String pv = properties.getProperty("netty.websocket.port");
        if (StringUtils.isNotEmpty(pv)) {
            port = Integer.parseInt(pv);
        }

        // 地址
        String pathV = properties.getProperty("netty.websocket.path");
        if (StringUtils.isNotEmpty(pathV)) {
            path = pathV;
        }

        // 消息最大帧体积
        String mv = properties.getProperty("netty.websocket.max-frame-siz");
        if (StringUtils.isNotEmpty(mv)) {
            maxFrameSize = Long.parseLong(mv);
        }

        // event loop group 线程数
        String wv = properties.getProperty("netty.websocket.worker-threads");
        if (StringUtils.isNotEmpty(wv)) {
            workerThreads = Integer.parseInt(wv);
        }

        String bv = properties.getProperty("netty.websocket.boss-threads");
        if (StringUtils.isNotEmpty(bv)) {
            bossThreads = Integer.parseInt(bv);
        }

        // 是否使用 epoll
        String ev = properties.getProperty("netty.websocket.epoll");
        if (StringUtils.isNotEmpty(ev)) {
            epoll = Boolean.parseBoolean(ev);
        }

    }


    public static void check() {

        log.info("\n" +
                "  _   _      _   _            _____ _           _      _____                          \n" +
                " | \\ | |    | | | |          / ____| |         | |    / ____|                         \n" +
                " |  \\| | ___| |_| |_ _   _  | |    | |__   __ _| |_  | (___   ___ _ ____   _____ _ __ \n" +
                " | . ` |/ _ \\ __| __| | | | | |    | '_ \\ / _` | __|  \\___ \\ / _ \\ '__\\ \\ / / _ \\ '__|\n" +
                " | |\\  |  __/ |_| |_| |_| | | |____| | | | (_| | |_   ____) |  __/ |   \\ V /  __/ |   \n" +
                " |_| \\_|\\___|\\__|\\__|\\__, |  \\_____|_| |_|\\__,_|\\__| |_____/ \\___|_|    \\_/ \\___|_|   \n" +
                "                      __/ |                                                           \n" +
                "                     |___/                                                            \n");
        log.info("chat server address:{}", address);
        log.info("chat server port:{}", port);
        log.info("chat server serializer:{}", serializer);
        log.info("chat server path:{}", path);
    }


}

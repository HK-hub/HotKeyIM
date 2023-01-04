package com.hk.im.server.chat.server;

import com.hk.im.server.chat.config.MetaDataConfig;
import com.hk.im.server.chat.server.handler.websocket.WebSocketProtocolChatChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @author : HK意境
 * @ClassName : HotKeyChatServer
 * @date : 2023/1/4 15:37
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class HotKeyChatServer {


    // 静态内部类
    private static class SingletonWebSocketServer {
        private static final HotKeyChatServer INSTANCE = new HotKeyChatServer();
    }

    // group 组
    private EventLoopGroup bossGroup = null;
    private EventLoopGroup workerGroup = null;
    private ServerBootstrap serverBootstrap = null;
    private Channel serverChannel = null;

    public HotKeyChatServer() {
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();
        this.initServer();
    }

    /**
     * 指定 worker 线程池的线程数,worker 数量
     * @param workers
     */
    public HotKeyChatServer(int workers) {
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup(workers);
        this.initServer();
    }


    /**
     * 初始化服务器配置
     */
    private void initServer() {
        serverBootstrap = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                // TCP_NODELAY算法，尽可能发送大块数据，减少充斥的小块数据
                .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                // 半连接队列+全连接队列数量
                .option(ChannelOption.SO_BACKLOG, 2048)
                // 自定义协议初始化器
                //.childHandler(new CustomProtocolChatChannelInitializer());
                // WebSocket 协议初始化器
                .childHandler(new WebSocketProtocolChatChannelInitializer());

    }


    /**
     * 启动 netty 服务器
     */
    public void runServer() {

        try{
            // 绑定端口
            ChannelFuture channelFuture = serverBootstrap.bind(MetaDataConfig.address, MetaDataConfig.port).sync();
            this.serverChannel = channelFuture.channel();
            // 服务启动成功
            log.info("websocket 服务启动，ip={},port={}", MetaDataConfig.address, MetaDataConfig.port);
            channelFuture.channel().closeFuture().sync();

        }catch(Exception e){
            e.printStackTrace();
            log.error("hot key chat server error...",e);
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


    public static HotKeyChatServer getInstance() {
        return SingletonWebSocketServer.INSTANCE;
    }

    public static void main(String[] args) {

        getInstance().runServer();


    }

}

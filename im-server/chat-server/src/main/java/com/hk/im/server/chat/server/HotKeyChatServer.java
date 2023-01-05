package com.hk.im.server.chat.server;

import com.hk.im.server.chat.config.MetaDataConfig;
import com.hk.im.server.chat.server.handler.websocket.WebSocketServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;

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
        this.initServer();
    }

    /**
     * 初始化服务器配置
     */
    private void initServer() {

        this.serverBootstrap = new ServerBootstrap();

        int bossThreads = MetaDataConfig.bossThreads;
        int workerThreads = MetaDataConfig.workerThreads;
        boolean epoll = MetaDataConfig.epoll;

        if (BooleanUtils.isTrue(epoll)) {
            // 使用epoll 模式
            bossGroup = new EpollEventLoopGroup(bossThreads,
                    new DefaultThreadFactory("WebSocketBossGroup", true));
            workerGroup = new EpollEventLoopGroup(workerThreads,
                    new DefaultThreadFactory("WebSocketWorkerGroup", true));
            this.serverBootstrap.channel(EpollServerSocketChannel.class);
        } else {
            bossGroup = new NioEventLoopGroup(bossThreads);
            workerGroup = new NioEventLoopGroup(workerThreads);
            this.serverBootstrap.channel(NioServerSocketChannel.class);
        }

        this.serverBootstrap
                .group(bossGroup, workerGroup)
                // TCP_NODELAY算法，尽可能发送大块数据，减少充斥的小块数据
                .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                // 半连接队列+全连接队列数量
                .option(ChannelOption.SO_BACKLOG, 2048)
                // 保持长连接
                .option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                // 自定义协议初始化器
                //.childHandler(new CustomProtocolChatChannelInitializer());
                // WebSocket 协议初始化器
                .childHandler(new WebSocketServerChannelInitializer());

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

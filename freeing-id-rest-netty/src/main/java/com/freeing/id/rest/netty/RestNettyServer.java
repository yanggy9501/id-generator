package com.freeing.id.rest.netty;

import com.freeing.id.rest.netty.handler.RestNettyServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * netty id 服务
 */
public class RestNettyServer {
    private static final Logger logger = LoggerFactory.getLogger(RestNettyServer.class);

    private final int port;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;

    public RestNettyServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        // Configure the server.
        bossGroup = new NioEventLoopGroup();
        workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new RestNettyServerInitializer());

            Channel ch = bootstrap.bind(port).sync().channel();
            logger.info("RestNettyServer started on port: {}.", port);

            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public void shutdown() {
        if (bossGroup != null && !bossGroup.isShuttingDown() && !bossGroup.isShutdown()) {
            bossGroup.shutdownGracefully();
        }
        if (workGroup != null && !workGroup.isShuttingDown() && !workGroup.isShutdown()) {
            workGroup.shutdownGracefully();
        }
        logger.info("RestNettyServer.shutdown|id server 停止");
    }
}

package com.freeing.id.rest.netty.handler;

import com.freeing.id.factory.IdServiceFactory;
import com.freeing.id.rest.netty.context.ApplicationHolder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * netty handler 初始化 handler
 */
public class RestNettyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline p = socketChannel.pipeline();
        p.addLast("codec", new HttpServerCodec());
        IdServiceFactory idServiceFactory = ApplicationHolder.getApplicationContext().getBean(IdServiceFactory.class);
        p.addLast("handler", new RestNettyServerHandler(idServiceFactory));
    }
}

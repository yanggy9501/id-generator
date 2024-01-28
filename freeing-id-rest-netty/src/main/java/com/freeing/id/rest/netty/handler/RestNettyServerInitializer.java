package com.freeing.id.rest.netty.handler;

import com.freeing.id.manager.IdManager;
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
        IdManager idManager = ApplicationHolder.getApplicationContext().getBean(IdManager.class);
        p.addLast("handler", new RestNettyServerHandler(idManager));
    }
}

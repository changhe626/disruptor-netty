package com.onyx.server.server;

import com.onyx.codec.MarshallingCodeCFactory;
import com.onyx.common.TranslatorData;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * linux 链接的句柄数的设置...
 * TCP握手原理和netty线程组的结合
 */
public class NettyServer {

    public NettyServer() {
        //创建2个线程组,一个用来接收,一个处理

        TranslatorData data = new TranslatorData();

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workGroup);
        bootstrap.option(ChannelOption.SO_BACKLOG,1024)
                //缓存区动态调配(自适应)
                .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                //缓冲对象池  缓存区
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                //日志的打印
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>(){
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                        sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                        sc.pipeline().addLast(new ServerHandle());
                    }
                });





    }
}

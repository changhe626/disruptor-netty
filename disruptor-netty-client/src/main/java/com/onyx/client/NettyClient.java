package com.onyx.client;

import com.onyx.codec.MarshallingCodeCFactory;
import com.onyx.common.TranslatorData;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyClient {


    private final static String ip="127.0.0.1";
    private final static int port=8765;
    private Channel channel;
    private EventLoopGroup workGroup;
    private ChannelFuture channelFuture;


    public NettyClient() {
        connect(ip,port);
    }

    private void connect(String ip, int port) {
        //创建一个线程组,实际处理业务
        workGroup = new NioEventLoopGroup();

        //辅助类.和Server不一样的.
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workGroup);


        bootstrap.channel(NioSocketChannel.class)
                //缓存区动态调配(自适应)
                .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                //缓冲对象池  缓存区
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                //日志的打印
                .handler(new LoggingHandler(LogLevel.INFO))
                .handler(new ChannelInitializer<SocketChannel>(){
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                        sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                        sc.pipeline().addLast(new ClientHandle());
                    }
                });


        //绑定端口,同步请求链接
        try {
            //一定要加上sync()
            channelFuture = bootstrap.connect(ip, port).sync();
            System.out.println("client connected");

            //进行数据发送,首先获取通道
            this.channel = channelFuture.channel();

            //数据的发送,可以对channel进行池化,节省资源.放到ThreadLocal中去.
            //放到ConcurrentHashMap中去,key就是string,value就是Channel.
            sendData();

            //
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {

        }
    }

    private void close() throws InterruptedException {
        channelFuture.channel().closeFuture().sync();
        //优雅的去关闭.
        workGroup.shutdownGracefully();
        System.out.println("client shutdown");
    }


    private void sendData() {
        for (int i = 0; i < 10; i++) {
            TranslatorData request = new TranslatorData();
            request.setId("id:"+i);
            request.setName("name:"+i);
            request.setMessage("message:"+i);
            channel.writeAndFlush(request);
        }
    }


}



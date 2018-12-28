package com.onyx.server.server;

import com.onyx.common.TranslatorData;
import com.onyx.disruptor.MessageProducer;
import com.onyx.disruptor.RingBufferWorkPoolFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandle extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        /*
        TranslatorData request=(TranslatorData)msg;
        System.out.println("Server端接收到的信息是:,"+request);
        //数据库持久化操作,  IO读写, -> 交给一个线程池异步的调用执行
        //对于netty 不管是server 还是client 的,都尽量不要其workgroup线程池中进行任务的执行,太消耗性能了.
        TranslatorData data = new TranslatorData();
        data.setId("response:"+request.getId());
        data.setName("response:"+request.getName());
        data.setMessage("response:"+request.getMessage());

        ctx.writeAndFlush(data);
        */

        TranslatorData request=(TranslatorData)msg;
        RingBufferWorkPoolFactory factory = RingBufferWorkPoolFactory.getInstance();
        //自己的应用服务的id生成规则

        String producerId="sessionID:001";
        MessageProducer producer = factory.getMessage(producerId);
        producer.onData(request,ctx);

    }
}

package com.onyx.client;

import com.onyx.common.TranslatorData;
import com.onyx.disruptor.MessageProducer;
import com.onyx.disruptor.RingBufferWorkPoolFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class ClientHandle extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /*
        try{
            TranslatorData response = (TranslatorData) msg;
            System.out.println("client:的接收到的是:"+response);
        }finally {
            //一定要释放,不然内存会爆炸...
            //用完了缓存要进行释放.
            ReferenceCountUtil.release(msg);
        }
        Disruptor是单独一个服务内部进行实现,而不是跨服务的应用.
        */

        TranslatorData response = (TranslatorData) msg;
        String producerId="code:sessionID:001";
        MessageProducer producer = RingBufferWorkPoolFactory.getInstance().getMessage(producerId);
        producer.onData(response,ctx);
    }
}

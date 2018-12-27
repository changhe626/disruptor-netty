package com.onyx.client;

import com.onyx.common.TranslatorData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class ClientHandle extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try{
            TranslatorData response = (TranslatorData) msg;
            System.out.println("client:的接收到的是:"+response);
        }finally {
            //一定要释放,不然内存会爆炸...
            //用完了缓存要进行释放.
            ReferenceCountUtil.release(msg);
        }
    }
}

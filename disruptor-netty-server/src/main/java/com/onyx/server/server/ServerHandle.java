package com.onyx.server.server;

import com.onyx.common.TranslatorData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandle extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        TranslatorData request=(TranslatorData)msg;
        System.out.println("Server端接收到的信息是:,"+request);
        TranslatorData data = new TranslatorData();
        data.setId("response:"+request.getId());
        data.setName("response:"+request.getName());
        data.setMessage("response:"+request.getMessage());

        ctx.writeAndFlush(data);

    }
}

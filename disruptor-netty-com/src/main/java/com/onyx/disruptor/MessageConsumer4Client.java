package com.onyx.disruptor;

import com.onyx.common.TranslatorData;
import com.onyx.common.TranslatorDataWapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

public class MessageConsumer4Client extends MessageConsumers {

    public MessageConsumer4Client(String consumerId) {
        super(consumerId);
    }

    @Override
    public void onEvent(TranslatorDataWapper event) throws Exception {
        ChannelHandlerContext ctx = event.getCtx();
        TranslatorData request = event.getData();
        try{
            //业务处理逻辑
            System.out.println("Client端接收到的信息是:,"+request);
        }finally {
            ReferenceCountUtil.release(event);
        }
    }
}

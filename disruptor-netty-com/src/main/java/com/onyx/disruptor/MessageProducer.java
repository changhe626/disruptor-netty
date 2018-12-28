package com.onyx.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.onyx.common.TranslatorData;
import com.onyx.common.TranslatorDataWapper;
import io.netty.channel.ChannelHandlerContext;

public class MessageProducer {

    private String producerId;
    private RingBuffer<TranslatorDataWapper> ringBuffer;
    public MessageProducer(String id,RingBuffer<TranslatorDataWapper> ringBuffer) {
        this.ringBuffer=ringBuffer;
        this.producerId=id;
    }

    public void onData(TranslatorData data, ChannelHandlerContext ctx){
        long next = ringBuffer.next();
        try {
            TranslatorDataWapper wapper = ringBuffer.get(next);
            wapper.setData(data);
            wapper.setCtx(ctx);
        }finally {
            ringBuffer.publish(next);
        }
    }


}

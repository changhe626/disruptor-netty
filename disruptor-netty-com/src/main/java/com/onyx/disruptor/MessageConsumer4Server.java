package com.onyx.disruptor;

import com.onyx.common.TranslatorData;
import com.onyx.common.TranslatorDataWapper;
import io.netty.channel.ChannelHandlerContext;

public class MessageConsumer4Server extends MessageConsumers {

    public MessageConsumer4Server(String consumerId) {
        super(consumerId);
    }

    @Override
    public void onEvent(TranslatorDataWapper event) throws Exception {
        //业务处理逻辑
        ChannelHandlerContext ctx = event.getCtx();
        TranslatorData request = event.getData();
        System.out.println("Server端接收到的信息是:,"+request);

        //回送响应
        //数据库持久化操作,  IO读写, -> 交给一个线程池异步的调用执行
        //对于netty 不管是server 还是client 的,都尽量不要其workgroup线程池中进行任务的执行,太消耗性能了.
        TranslatorData data = new TranslatorData();
        data.setId("response:"+request.getId());
        data.setName("response:"+request.getName());
        data.setMessage("response:"+request.getMessage());
        ctx.writeAndFlush(data);
    }
}

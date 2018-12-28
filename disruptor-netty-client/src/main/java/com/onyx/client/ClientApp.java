package com.onyx.client;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import com.onyx.disruptor.MessageConsumer4Client;
import com.onyx.disruptor.MessageConsumers;
import com.onyx.disruptor.RingBufferWorkPoolFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApp {

    public static void main(String[] args) {
        SpringApplication.run(ClientApp.class, args);


        MessageConsumers[] messageConsumers=new MessageConsumers[4];
        for (int i = 0; i < 4; i++) {
            MessageConsumer4Client client = new MessageConsumer4Client("client:code:session:001");
            messageConsumers[i]=client;
        }

        RingBufferWorkPoolFactory.getInstance().initAndStart(
                ProducerType.MULTI,
                1024*1024,
                new YieldingWaitStrategy(),   // 性能最高的.
                //new BlockingWaitStrategy(),  性能最低
                messageConsumers
        );


        new NettyClient();
    }


}


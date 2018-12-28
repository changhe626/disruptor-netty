package com.onyx.server;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import com.onyx.disruptor.MessageConsumer4Server;
import com.onyx.disruptor.MessageConsumers;
import com.onyx.disruptor.RingBufferWorkPoolFactory;
import com.onyx.server.server.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Server {

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);


        MessageConsumers[] messageConsumers=new MessageConsumers[4];
        for (int i = 0; i < 4; i++) {
            MessageConsumer4Server consumer4Server = new MessageConsumer4Server("code:sessionId:003");
            messageConsumers[i]=consumer4Server;
        }

        RingBufferWorkPoolFactory.getInstance().initAndStart(
                ProducerType.MULTI,
                1024*1024,
                new YieldingWaitStrategy(),   // 性能最高的.
                //new BlockingWaitStrategy(),  性能最低
                messageConsumers
        );



        
        new NettyServer();
    }
}

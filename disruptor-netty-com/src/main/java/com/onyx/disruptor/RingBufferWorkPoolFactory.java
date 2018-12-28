package com.onyx.disruptor;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.WorkerPool;
import com.lmax.disruptor.dsl.ProducerType;
import com.onyx.common.TranslatorDataWapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Executors;

public class RingBufferWorkPoolFactory {

    static class SingletonHolder{
        final static RingBufferWorkPoolFactory instance = new RingBufferWorkPoolFactory();
    }

    private RingBufferWorkPoolFactory() {
    }

    public static RingBufferWorkPoolFactory getInstance() {
        return SingletonHolder.instance;
    }

    private static Map<String,MessageProducer> producers=new ConcurrentHashMap<>();

    private static Map<String,MessageConsumers> consumers=new ConcurrentHashMap<>();

    private RingBuffer<TranslatorDataWapper> ringBuffer;
    private WorkerPool<TranslatorDataWapper> workerPool;
    private SequenceBarrier sequenceBarrier;


    public void initAndStart(ProducerType producerType, int bufferSize, WaitStrategy waitStrategy,MessageConsumers[] messageConsumers){
        //1.构建ringBuffer对象
        ringBuffer = RingBuffer.create(producerType,
                new EventFactory<TranslatorDataWapper>() {
                    @Override
                    public TranslatorDataWapper newInstance() {
                        return new TranslatorDataWapper();
                    }
                },
                bufferSize,
                waitStrategy
        );
        //2.设置序号栅栏
        sequenceBarrier=ringBuffer.newBarrier();
        //3.设置工作池
        workerPool=new WorkerPool<TranslatorDataWapper>(ringBuffer,sequenceBarrier,new EventExceptionHandle(),messageConsumers);
        //4.把所构建的消费者置入池子中
        for (MessageConsumers consumer : messageConsumers) {
            consumers.put(consumer.getConsumerId(),consumer);
        }
        //5.添加我们的sequences
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
        //6.启动工作池
        workerPool.start(Executors.newFixedThreadPool(4));
    }


    public MessageProducer getMessage(String producerId){
        MessageProducer producer = producers.get(producerId);
        if(producer==null){
            producer=new MessageProducer(producerId,ringBuffer);
            producers.put(producerId,producer);
        }
        return producer;
    }

    /**
     * 异常的处理类..
     */
    static class EventExceptionHandle implements ExceptionHandler<TranslatorDataWapper>{

        @Override
        public void handleEventException(Throwable ex, long sequence, TranslatorDataWapper event) {

        }

        @Override
        public void handleOnStartException(Throwable ex) {

        }

        @Override
        public void handleOnShutdownException(Throwable ex) {

        }
    }



}

package com.onyx.disruptor;

import com.lmax.disruptor.WorkHandler;
import com.onyx.common.TranslatorDataWapper;

/**
 * onEvent  由子类去实现.
 */
public abstract class MessageConsumers implements WorkHandler<TranslatorDataWapper> {

    protected String consumerId;

    public MessageConsumers(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }
}

package com.onyx.common;

import java.io.Serializable;

public class TranslatorData implements Serializable {

    private String id;
    private String name;
    //消息内容
    private String message;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

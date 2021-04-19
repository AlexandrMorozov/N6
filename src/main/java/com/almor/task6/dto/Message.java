package com.almor.task6.dto;

import lombok.Data;

@Data
public abstract class Message {

    public Message() {

    }

    public Message(String action) {
        this.action = action;
    }

    protected String action;

}

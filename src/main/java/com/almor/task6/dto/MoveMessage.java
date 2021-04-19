package com.almor.task6.dto;

import lombok.Data;

@Data
public class MoveMessage extends Message {

    public MoveMessage() {
    }

    public MoveMessage(String action, String from, String to, String gameName, int x, int y) {
        super(action);
        this.from = from;
        this.to = to;
        this.gameName = gameName;
        this.x = x;
        this.y = y;
    }

    private String from;

    private String to;

    private String gameName;

    private int x;

    private int y;

    private char result;

}

package com.almor.task6.dto;

import lombok.Data;

@Data
public class GameResultMessage extends Message {

    public GameResultMessage() {

    }

    public GameResultMessage(String action, String gameResult) {
        super(action);
        this.gameResult = gameResult;
    }

    private String gameResult;

}

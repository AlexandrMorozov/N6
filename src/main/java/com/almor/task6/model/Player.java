package com.almor.task6.model;

import lombok.Data;

@Data
public class Player extends User {

    public Player(String name) {
        super(name);
    }

    private char gameSymbol;

}

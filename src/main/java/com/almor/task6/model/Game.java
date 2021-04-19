package com.almor.task6.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Game {

    public Game() {

    }

    public Game(String name, String[] tags, String author) {
        this.name = name;
        this.tags = tags;
        this.author = author;
        this.gameField = new char[3][3];
        players = new ArrayList<>();
    }

    public Game(String name) {
        this.name = name;
    }

    private String name;

    private String author;

    private String[] tags;

    private char[][] gameField;

    private ArrayList<Player> players;

    private int currentTurn = 0;

    public void addPlayer(Player player) {
        if (players.size() < 2) {
            players.add(player);
        }
    }

    public boolean addMove(int x, int y, char move) {
        if(checkTile(x, y)) {
            gameField[x][y] = move;
            return true;
        }
        return false;
    }

    private boolean checkTile(int x, int y) {
        if (gameField[x][y] != 0) {
            return false;
        }
        return true;
    }

    public Player findPlayer(String name) {
        Player player = null;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getName().equals(name)) {
                player = players.get(i);
                break;
            }
        }
        return player;
    }

    public String getPlayerName(int index) {
        if (players.size() > index) {
            return players.get(index).getName();
        }
        return null;
    }

}

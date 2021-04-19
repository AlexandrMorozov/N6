package com.almor.task6.service;

import org.springframework.stereotype.Service;

@Service
public class VictoryConditionsService {

    //Victory conditions
    //Diagonal conditions
    public boolean checkDiagonalVictoryConditions(char[][] field, char symbol) {

        if((field[0][0]==symbol && field[1][1]==symbol && field[2][2]==symbol)
                || (field[0][2]==symbol && field[1][1]==symbol && field[2][0]==symbol)) {
            return true;
        }
        return false;
    }

    //Horizontal and vertical conditions
    public boolean checkLinearVictoryConditions(boolean isVertical, char[][] field, char symbol) {

        for (int i = 0; i < field.length; i++) {
            int counter = 0;
            for (int j = 0; j < field[i].length; j++) {
                char currentSymbol = isVertical ? field[i][j] : field[j][i];
                counter = checkSymbolsMatching(currentSymbol, symbol, counter);
            }

            if (counter == field.length) {
                return true;
            }
        }

        return false;
    }

    //Draw
    public boolean checkDrawConditions(char[][] field)
    {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if(field[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private int checkSymbolsMatching(char a, char b, int counter) {
        if (a == b) {
            counter++;
        }
        return counter;
    }

}

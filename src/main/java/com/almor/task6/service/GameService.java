package com.almor.task6.service;

import com.almor.task6.dto.GameResultMessage;
import com.almor.task6.dto.MoveMessage;
import com.almor.task6.model.Game;
import com.almor.task6.model.Player;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Data
public class GameService {

    private HashMap<String, Game> games = new HashMap<>();
    @Autowired
    private VictoryConditionsService victoryConditions;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public void startNewGame(String gameName, String[] tags, String author) {
        games.put(gameName, new Game(gameName, tags, author));
    }

    public void endGame(MoveMessage msg) {
        games.remove(msg.getGameName());
    }

    public void addPlayer(String name, String gameName) {

        int numOfPlayers = games.get(gameName).getPlayers().size();
        Player newPlayer = new Player(name);

        if (numOfPlayers == 0) {
            newPlayer.setGameSymbol('X');
        } else if (numOfPlayers == 1) {
            newPlayer.setGameSymbol('O');
        }

        games.get(gameName).addPlayer(newPlayer);
    }

    public boolean makeMove(MoveMessage msg) {

        Game game = games.get(msg.getGameName());
        Player player = game.findPlayer(msg.getFrom());

        if (game.getPlayers().indexOf(player) == game.getCurrentTurn() &&
                game.addMove(msg.getX(), msg.getY(), player.getGameSymbol())) {

            game.setCurrentTurn(game.getPlayers().indexOf(player) == 1 ? 0 : 1);
            msg.setAction("move");
            msg.setResult(player.getGameSymbol());

            sendMessage(msg.getFrom(), msg);
            sendMessage(msg.getTo(), msg);
            return true;
        }
        return false;
    }

    public boolean isThereFreePlaces(String gameName) {
        if (games.get(gameName).getPlayers().size() < 2) {
            return true;
        }
        return false;
    }

    public boolean isGameExists(String gameName) {
        if (games.get(gameName) != null) {
            return true;
        }
        return false;
    }

    public boolean isGameParticipant(String gameName, String playerName) {
        if (games.get(gameName).findPlayer(playerName) == null) {
            return false;
        }
        return true;
    }

    public boolean isGameEnded(MoveMessage msg) {
        Game game = games.get(msg.getGameName());
        char symbol = game.findPlayer(msg.getFrom()).getGameSymbol();

        if (victoryConditions.checkDiagonalVictoryConditions(game.getGameField(), symbol)
                || victoryConditions.checkLinearVictoryConditions(true, game.getGameField(), symbol)
                || victoryConditions.checkLinearVictoryConditions(false, game.getGameField(), symbol)) {

            sendMessage(msg.getFrom(), new GameResultMessage("endgame", "You win!"));
            sendMessage(msg.getTo(), new GameResultMessage("endgame", "You lost!"));
            return true;
        } else if (victoryConditions.checkDrawConditions(game.getGameField())) {
            sendMessage(msg.getFrom(), new GameResultMessage("endgame", "Draw!"));
            sendMessage(msg.getTo(), new GameResultMessage("endgame", "Draw!"));
            return true;
        }
        return false;
    }

    //Send message with game result
    private void sendMessage(String receiver, GameResultMessage message) {
        simpMessagingTemplate.convertAndSendToUser(receiver,
                "/secured/user/queue/sp-user", message);
    }

    //Send message with move result
    public void sendMessage(String receiver, MoveMessage message) {
        simpMessagingTemplate.convertAndSendToUser(receiver,
                "/secured/user/queue/sp-user", message);
    }

    public String getPlayerName(String gameName, int index) {
        return games.get(gameName).getPlayerName(index);
    }

    public String getGameAuthor(String gameName) {
        return games.get(gameName).getAuthor();
    }

}

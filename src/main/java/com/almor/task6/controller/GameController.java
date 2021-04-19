package com.almor.task6.controller;

import com.almor.task6.dto.MoveMessage;
import com.almor.task6.model.Game;
import com.almor.task6.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GameController {

    @Autowired
    private GameService gameService;

    @MessageMapping("/secured/game")
    public void sendToUser(@Payload MoveMessage msg) throws Exception {

        if (gameService.makeMove(msg)) {
            if (gameService.isGameEnded(msg)) {
                gameService.endGame(msg);
            }
        }
    }

    @GetMapping("/")
    public String getMenu(Model model) {
        model.addAttribute("games", gameService.getGames().values());
        return "menu";
    }

    @PostMapping("/")
    public String filterGames(String tags, Model model) {
        ArrayList<String> allTags = new ArrayList<>(Arrays.asList(tags.split(" ")));
        Collection<Game> allGames = gameService.getGames().values();

        //Filtration by tags
        List<Game> filteredGames = allGames.stream().filter(game -> Arrays.stream(game.getTags())
                        .anyMatch(allTags :: contains)).collect(Collectors.toList());
        model.addAttribute("games", filteredGames);
        return "menu";
    }

    @PostMapping("/game")
    public String initGame(String name, String tags) {

        if (!gameService.isGameExists(name) && !name.equals("")) {
            String[] allTags = tags.split(" ");
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            gameService.startNewGame(name, allTags, userName);
            gameService.addPlayer(userName, name);

            return "redirect:/game?name=" + name;
        }

        return "redirect:/";
    }

    @GetMapping("/game")
    public String joinGame(@RequestParam(required = false) String name, Model model) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        if (gameService.isGameExists(name)) {

            if (gameService.isGameParticipant(name, userName)) {
                model.addAttribute("gamename", name);
                model.addAttribute("player1", userName);

                if (!gameService.isThereFreePlaces(name)) {
                    int secondPlayerIndex = gameService.getGameAuthor(name).equals(userName)? 0 : 1;
                    model.addAttribute("player2", gameService.getPlayerName(name, secondPlayerIndex));
                }

                return "game";

            } else if (gameService.isThereFreePlaces(name)) {
                gameService.addPlayer(userName, name);
                model.addAttribute("gamename", name);
                model.addAttribute("player1", userName);
                model.addAttribute("player2", gameService.getGameAuthor(name));

                MoveMessage message = new MoveMessage();
                message.setAction("addname");
                message.setFrom(userName);

                gameService.sendMessage(gameService.getGameAuthor(name), message);

                return "game";
            }
        }

        return "redirect:/";
    }
}

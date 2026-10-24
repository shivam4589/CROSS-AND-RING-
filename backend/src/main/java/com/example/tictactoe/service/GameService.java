package com.example.tictactoe.service;

import com.example.tictactoe.dto.CreateGameRequest;
import com.example.tictactoe.dto.GameResponse;
import com.example.tictactoe.dto.MakeMoveRequest;
import com.example.tictactoe.model.Game;
import com.example.tictactoe.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameResponse createGame(CreateGameRequest request) {
        String pX = "Player X";
        String pO = "Player O";

        if (request != null) {
            if (request.getPlayerX() != null && !request.getPlayerX().trim().isEmpty()) {
                pX = request.getPlayerX().trim();
            }
            if (request.getPlayerO() != null && !request.getPlayerO().trim().isEmpty()) {
                pO = request.getPlayerO().trim();
            }
        }

        Game game = new Game(pX, pO);
        Game saved = gameRepository.save(game);
        return buildResponse(saved, null);
    }

    public GameResponse getGame(Long id) {
        Game game = gameRepository.findById(id).orElse(null);
        if (game == null) {
            return null;
        }
        int[] winningLine = findWinningLine(game.getBoard());
        return buildResponse(game, winningLine);
    }

    public List<GameResponse> getAllGames() {
        List<Game> games = gameRepository.findAllByOrderByIdDesc();
        List<GameResponse> responses = new ArrayList<>();

        for (Game game : games) {
            int[] line = findWinningLine(game.getBoard());
            responses.add(buildResponse(game, line));
        }

        return responses;
    }

    public GameResponse makeMove(Long id, MakeMoveRequest request) {
        Game game = gameRepository.findById(id).orElse(null);
        if (game == null) {
            throw new RuntimeException("Game not found");
        }

        if (!"IN_PROGRESS".equals(game.getStatus())) {
            throw new RuntimeException("Game is already over");
        }

        String player = request.getPlayer().toUpperCase();
        if (!player.equals(game.getCurrentTurn())) {
            throw new RuntimeException("Not your turn");
        }

        int index = request.getCellIndex();
        if (index < 0 || index > 8) {
            throw new RuntimeException("Invalid board position");
        }

        char[] chars = game.getBoard().toCharArray();
        if (chars[index] != ' ') {
            throw new RuntimeException("Cell already taken");
        }

        chars[index] = player.charAt(0);
        game.setBoard(new String(chars));

        int[] winningLine = findWinningLine(game.getBoard());

        if (winningLine != null) {
            game.setStatus(player.equals("X") ? "X_WON" : "O_WON");
            game.setWinner(player);
        } else if (!game.getBoard().contains(" ")) {
            game.setStatus("DRAW");
            game.setWinner("NONE");
        } else {
            game.setCurrentTurn(player.equals("X") ? "O" : "X");
        }

        Game saved = gameRepository.save(game);
        return buildResponse(saved, winningLine);
    }

    private int[] findWinningLine(String board) {
        if (board == null || board.length() < 9) {
            return null;
        }

        int[][] combos = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
        };

        for (int i = 0; i < combos.length; i++) {
            int a = combos[i][0];
            int b = combos[i][1];
            int c = combos[i][2];

            char c1 = board.charAt(a);
            char c2 = board.charAt(b);
            char c3 = board.charAt(c);

            if (c1 != ' ' && c1 == c2 && c2 == c3) {
                return combos[i];
            }
        }

        return null;
    }

    private GameResponse buildResponse(Game game, int[] winningLine) {
        GameResponse res = new GameResponse();
        res.setId(game.getId());
        res.setPlayerX(game.getPlayerX());
        res.setPlayerO(game.getPlayerO());
        res.setCurrentTurn(game.getCurrentTurn());
        res.setStatus(game.getStatus());
        res.setWinner(game.getWinner());
        res.setWinningLine(winningLine);
        res.setCreatedAt(game.getCreatedAt());

        String[] boardArray = new String[9];
        for (int i = 0; i < 9; i++) {
            char ch = game.getBoard().charAt(i);
            boardArray[i] = (ch == ' ') ? "" : String.valueOf(ch);
        }
        res.setBoard(boardArray);

        return res;
    }
}

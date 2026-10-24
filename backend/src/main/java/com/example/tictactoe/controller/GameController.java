package com.example.tictactoe.controller;

import com.example.tictactoe.dto.CreateGameRequest;
import com.example.tictactoe.dto.GameResponse;
import com.example.tictactoe.dto.MakeMoveRequest;
import com.example.tictactoe.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "*")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<GameResponse> createGame(@RequestBody(required = false) CreateGameRequest request) {
        GameResponse response = gameService.createGame(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameResponse> getGame(@PathVariable Long id) {
        GameResponse response = gameService.getGame(id);
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/moves")
    public ResponseEntity<?> makeMove(@PathVariable Long id, @RequestBody MakeMoveRequest request) {
        try {
            GameResponse response = gameService.makeMove(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping
    public ResponseEntity<List<GameResponse>> getAllGames() {
        List<GameResponse> list = gameService.getAllGames();
        return ResponseEntity.ok(list);
    }
}

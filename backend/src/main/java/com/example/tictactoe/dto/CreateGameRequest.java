package com.example.tictactoe.dto;

public class CreateGameRequest {
    private String playerX;
    private String playerO;

    public CreateGameRequest() {
    }

    public CreateGameRequest(String playerX, String playerO) {
        this.playerX = playerX;
        this.playerO = playerO;
    }

    public String getPlayerX() {
        return playerX;
    }

    public void setPlayerX(String playerX) {
        this.playerX = playerX;
    }

    public String getPlayerO() {
        return playerO;
    }

    public void setPlayerO(String playerO) {
        this.playerO = playerO;
    }
}

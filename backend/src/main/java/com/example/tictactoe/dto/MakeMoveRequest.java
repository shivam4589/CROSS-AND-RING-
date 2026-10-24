package com.example.tictactoe.dto;

public class MakeMoveRequest {
    private String player;
    private int cellIndex;

    public MakeMoveRequest() {
    }

    public MakeMoveRequest(String player, int cellIndex) {
        this.player = player;
        this.cellIndex = cellIndex;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getCellIndex() {
        return cellIndex;
    }

    public void setCellIndex(int cellIndex) {
        this.cellIndex = cellIndex;
    }
}

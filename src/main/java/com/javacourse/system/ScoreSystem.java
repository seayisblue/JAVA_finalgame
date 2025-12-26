package com.javacourse.system;

public class ScoreSystem {
    private int player1Score;
    private int player2Score;

    public ScoreSystem() {
        this.player1Score = 0;
        this.player2Score = 0;
    }

    //TODO: 硬编码了playerID.
    public void addScore(int playerId, int score) {
        if (playerId == 0) {
            player1Score += score;
        } else if (playerId == 1) {
            player2Score += score;
        }
    }

    public void resetScores() {
        this.player1Score = 0;
        this.player2Score = 0;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }
}

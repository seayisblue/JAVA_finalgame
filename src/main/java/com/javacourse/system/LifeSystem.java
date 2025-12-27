package com.javacourse.system;

public class LifeSystem {
    private int player1Lives;
    private int player2Lives;

    public LifeSystem(int initialLives) {
        resetLives(initialLives);
    }

    public void resetLives(int livesPerPlayer) {
        this.player1Lives = livesPerPlayer;
        this.player2Lives = livesPerPlayer;
    }

    public void damagePlayer(int playerId, int damage) {
        if (playerId == 0) {
            player1Lives = Math.max(0, player1Lives - damage);
        } else if (playerId == 1) {
            player2Lives = Math.max(0, player2Lives - damage);
        }
    }

    public int getPlayer1Lives() {
        return player1Lives;
    }

    public int getPlayer2Lives() {
        return player2Lives;
    }
}

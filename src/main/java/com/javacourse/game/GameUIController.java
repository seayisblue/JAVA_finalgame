package com.javacourse.game;

public interface GameUIController {
    GameState getGameState();

    GameMode getGameMode();

    Difficulty getDifficulty();

    int getSingleHighScore();

    int getDoubleHighScore();

    boolean isNewRecord();

    void startGame();

    void resumeGame();

    void exitToMenu();

    void restartGame();

    void setGameMode(GameMode mode);

    void setDifficulty(Difficulty difficulty);
}

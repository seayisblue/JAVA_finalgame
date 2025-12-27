package com.javacourse.game;

import com.javacourse.config.Constants;
import com.javacourse.controller.PlayerController;
import com.javacourse.entity.DepthCharge;
import com.javacourse.entity.Ship;
import com.javacourse.factory.EntityFactory;
import com.javacourse.framework.core.GameLoop;
import com.javacourse.framework.core.GameWorld;
import com.javacourse.system.CollisionHandler;
import com.javacourse.system.LifeSystem;
import com.javacourse.system.ScoreSystem;
import com.javacourse.system.SubmarineAttackSystem;
import com.javacourse.system.SubmarineSpawner;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SubmarineWarGame implements GameUIController {
    private final GameWorld world;
    private final GameLoop gameLoop;
    private final GamePanel gamePanel;

    private EntityFactory factory;

    private Ship player1Ship;
    private Ship player2Ship;
    private PlayerController player1Controller;
    private PlayerController player2Controller;

    private SubmarineSpawner submarineSpawner;
    private CollisionHandler collisionHandler;
    private ScoreSystem scoreSystem;
    private LifeSystem lifeSystem;
    private SubmarineAttackSystem submarineAttackSystem;

    private GameState gameState;
    private GameMode gameMode;
    private Difficulty difficulty;
    private int singleHighScore;
    private int doubleHighScore;
    private boolean newRecord;

    public SubmarineWarGame() {
        world = new GameWorld(
                Constants.WINDOW_WIDTH,
                Constants.WINDOW_HEIGHT
        );

        factory = new EntityFactory();
        scoreSystem = new ScoreSystem();
        lifeSystem = new LifeSystem(Constants.DEFAULT_LIVES);
        gameState = GameState.START;
        gameMode = GameMode.SINGLE;
        difficulty = Difficulty.NORMAL;
        newRecord = false;

        gamePanel = new GamePanel(world, scoreSystem, lifeSystem, this);
        initPanel();

        gameLoop = new GameLoop(world, () -> {
            updateGame();
            gamePanel.refresh();
        }, Constants.DEFAULT_FPS);
    }

    public void start() {
        gameLoop.start();
        world.stop();
    }

    public void stop() {
        gameLoop.stop();
        world.stop();
    }

    public JPanel getGamePanel() {
        return gamePanel;
    }

    private void initSystems() {
        submarineSpawner = new SubmarineSpawner(factory, world);
        collisionHandler = new CollisionHandler(world, factory, scoreSystem, lifeSystem);
        submarineAttackSystem = new SubmarineAttackSystem(factory, world);
        submarineSpawner.applyDifficulty(difficulty);
    }

    private void initPlayers() {

        int ID = 0;

        player1Ship = factory.createShip(ID,
                Constants.SHIP1_SPAWN_X, Constants.SHIP1_SPAWN_Y);

        world.addEntity(player1Ship);
        if (gameMode == GameMode.DOUBLE) {
            player2Ship = factory.createShip(1,
                    Constants.SHIP2_SPAWN_X, Constants.SHIP2_SPAWN_Y);
            world.addEntity(player2Ship);
        } else {
            player2Ship = null;
        }
    }

    private void initPanel() {
        gamePanel.setFocusable(true);
        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    handlePauseToggle();
                }
            }
        });
    }

    private void configureControllers() {
        if (player1Controller != null) {
            gamePanel.removeKeyListener(player1Controller);
        }
        if (player2Controller != null) {
            gamePanel.removeKeyListener(player2Controller);
        }

        int[] player1LeftKeys;
        int[] player1RightKeys;
        int[] player1FireKeys;
        if (gameMode == GameMode.SINGLE) {
            player1LeftKeys = new int[]{Constants.SHIP1_LEFT_KEY, Constants.SHIP2_LEFT_KEY};
            player1RightKeys = new int[]{Constants.SHIP1_RIGHT_KEY, Constants.SHIP2_RIGHT_KEY};
            player1FireKeys = new int[]{Constants.SHIP1_FIRE_KEY, Constants.SHIP2_FIRE_KEY};
        } else {
            player1LeftKeys = new int[]{Constants.SHIP1_LEFT_KEY};
            player1RightKeys = new int[]{Constants.SHIP1_RIGHT_KEY};
            player1FireKeys = new int[]{Constants.SHIP1_FIRE_KEY};
        }

        player1Controller = new PlayerController(
                player1Ship,
                player1LeftKeys,
                player1RightKeys,
                player1FireKeys
        );
        player1Controller.setOnFire(() -> fireBomb(player1Ship));
        gamePanel.addKeyListener(player1Controller);

        if (player2Ship != null) {
            player2Controller = new PlayerController(
                    player2Ship,
                    new int[]{Constants.SHIP2_LEFT_KEY},
                    new int[]{Constants.SHIP2_RIGHT_KEY},
                    new int[]{Constants.SHIP2_FIRE_KEY}
            );
            player2Controller.setOnFire(() -> fireBomb(player2Ship));
            gamePanel.addKeyListener(player2Controller);
        } else {
            player2Controller = null;
        }
        gamePanel.requestFocusInWindow();
    }

    private void fireBomb(Ship ship) {
        if (!ship.tryFire()) return;

        double x = ship.getBombSpawnX();
        double y = ship.getBombSpawnY();
        DepthCharge bomb = factory.createBomb(x,y,ship.getPlayerId());
        world.addEntity(bomb);
    }

    private void updateGame() {
        if (gameState != GameState.RUNNING) {
            return;
        }
        submarineSpawner.update();
        submarineAttackSystem.update();
        collisionHandler.update();
        checkGameOver();
    }

    private void checkGameOver() {
        if (gameMode == GameMode.SINGLE) {
            if (lifeSystem.getPlayer1Lives() <= 0) {
                triggerGameOver();
            }
        } else if (gameMode == GameMode.DOUBLE) {
            if (lifeSystem.getPlayer1Lives() <= 0 || lifeSystem.getPlayer2Lives() <= 0) {
                triggerGameOver();
            }
        }
    }

    private void triggerGameOver() {
        if (gameState == GameState.GAME_OVER) {
            return;
        }
        gameState = GameState.GAME_OVER;
        world.stop();
        updateHighScores();
    }

    private void updateHighScores() {
        newRecord = false;
        if (gameMode == GameMode.SINGLE) {
            int score = scoreSystem.getPlayer1Score();
            if (score > singleHighScore) {
                singleHighScore = score;
                newRecord = true;
            }
        } else if (gameMode == GameMode.DOUBLE) {
            int total = scoreSystem.getPlayer1Score() + scoreSystem.getPlayer2Score();
            if (total > doubleHighScore) {
                doubleHighScore = total;
                newRecord = true;
            }
        }
    }

    private void resetGameData() {
        scoreSystem.resetScores();
        lifeSystem.resetLives(Constants.DEFAULT_LIVES);
        newRecord = false;
    }

    private void setupNewGame() {
        world.clear();
        factory = new EntityFactory();
        initPlayers();
        initSystems();
        resetGameData();
        configureControllers();
        if (submarineSpawner != null) {
            submarineSpawner.reset();
        }
        gameState = GameState.RUNNING;
        world.start();
    }

    private void handlePauseToggle() {
        if (gameState == GameState.RUNNING) {
            gameState = GameState.PAUSED;
            world.stop();
        } else if (gameState == GameState.PAUSED) {
            resumeGame();
        }
    }

    @Override
    public GameState getGameState() {
        return gameState;
    }

    @Override
    public GameMode getGameMode() {
        return gameMode;
    }

    @Override
    public Difficulty getDifficulty() {
        return difficulty;
    }

    @Override
    public int getSingleHighScore() {
        return singleHighScore;
    }

    @Override
    public int getDoubleHighScore() {
        return doubleHighScore;
    }

    @Override
    public boolean isNewRecord() {
        return newRecord;
    }

    @Override
    public void startGame() {
        setupNewGame();
    }

    @Override
    public void resumeGame() {
        if (gameState == GameState.PAUSED) {
            gameState = GameState.RUNNING;
            world.start();
        }
    }

    @Override
    public void exitToMenu() {
        gameState = GameState.START;
        world.stop();
        world.clear();
        resetGameData();
        gameMode = GameMode.SINGLE;
        difficulty = Difficulty.NORMAL;
    }

    @Override
    public void restartGame() {
        setupNewGame();
    }

    @Override
    public void setGameMode(GameMode mode) {
        if (mode != null) {
            gameMode = mode;
        }
    }

    @Override
    public void setDifficulty(Difficulty difficulty) {
        if (difficulty != null) {
            this.difficulty = difficulty;
        }
    }

}

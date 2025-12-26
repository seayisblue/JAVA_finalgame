package com.javacourse.game;

import com.javacourse.controller.PlayerController;
import com.javacourse.entity.DepthCharge;
import com.javacourse.entity.Ship;
import com.javacourse.factory.EntityFactory;
import com.javacourse.framework.core.GameLoop;
import com.javacourse.framework.core.GameWorld;
import com.javacourse.config.Constants;
import com.javacourse.system.CollisionHandler;
import com.javacourse.system.ScoreSystem;
import com.javacourse.system.SubmarineAttackSystem;
import com.javacourse.system.SubmarineSpawner;

import javax.swing.*;

public class SubmarineWarGame {
    private GameWorld world;
    private GameLoop gameLoop;
    private GamePanel gamePanel;

    private EntityFactory factory;

    private Ship player1Ship;
    private PlayerController player1Controller;

    private SubmarineSpawner submarineSpawner;
    private CollisionHandler collisionHandler;
    private ScoreSystem scoreSystem;
    private SubmarineAttackSystem submarineAttackSystem;

    public SubmarineWarGame() {
        world = new GameWorld(
                Constants.WINDOW_WIDTH,
                Constants.WINDOW_HEIGHT
        );

        factory = new EntityFactory();
        initPlayers();
        initSystems();
        initPanel();

        gameLoop = new GameLoop(world, ()->{
            updateGame();
            gamePanel.refresh();
        }, Constants.DEFAULT_FPS);
    }

    public void start() {
        world.start();
        gameLoop.start();
    }

    public void stop() {
        gameLoop.stop();
        world.stop();
    }

    public JPanel getGamePanel() {
        return gamePanel;
    }

    private void initSystems() {
        scoreSystem = new ScoreSystem();
        submarineSpawner = new SubmarineSpawner(factory, world);
        collisionHandler = new CollisionHandler(world, factory, scoreSystem);
        submarineAttackSystem = new SubmarineAttackSystem(factory, world, player1Ship);
    }

    private void initPlayers() {

        int ID = 0;

        player1Ship = factory.createShip(ID,
                Constants.SHIP1_SPAWN_X, Constants.SHIP1_SPAWN_Y);

        world.addEntity(player1Ship);
        player1Controller = new PlayerController(
                player1Ship,
                Constants.SHIP1_LEFT_KEY,
                Constants.SHIP1_RIGHT_KEY,
                Constants.SHIP1_FIRE_KEY
        );
        player1Controller.setOnFire(() -> fireBomb(player1Ship));

    }

    private void initPanel() {
        gamePanel = new GamePanel(world, scoreSystem);
        gamePanel.addKeyListener(player1Controller);
        gamePanel.setFocusable(true);
    }


    private void fireBomb(Ship ship) {
        if (!ship.tryFire()) return;

        double x = ship.getBombSpawnX();
        double y = ship.getBombSpawnY();
        DepthCharge bomb = factory.createBomb(x,y,ship.getPlayerId());
        world.addEntity(bomb);
    }

    private void updateGame() {
        submarineSpawner.update();
        submarineAttackSystem.update();
        collisionHandler.update();
    }



}

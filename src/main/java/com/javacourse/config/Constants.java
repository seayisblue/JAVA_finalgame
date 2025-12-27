package com.javacourse.config;

import java.awt.event.KeyEvent;

public class Constants {

    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    public static final int MAP_WIDTH = 1000;
    public static final int MAP_HEIGHT = 700;
    public static final int DEFAULT_FPS = 60;

    public static final int SEA_LEVEL = 262;
    public static final int SUBMARINE_ZONE_TOP = 272;
    public static final int SUBMARINE_ZONE_BOTTOM = 550;

    public static final int SHIP_WIDTH = 80;
    public static final int SHIP_HEIGHT = 40;
    public static final int SHIP_SPEED = 5;
    public static final int SHIP1_SPAWN_X = 200;
    public static final int SHIP1_SPAWN_Y = 160;
    public static final int SHIP2_SPAWN_X = 600;
    public static final int SHIP2_SPAWN_Y = 160;

    public static final int SHIP1_LEFT_KEY = KeyEvent.VK_A;
    public static final int SHIP1_RIGHT_KEY = KeyEvent.VK_D;
    public static final int SHIP1_FIRE_KEY = KeyEvent.VK_SPACE;
    public static final int SHIP2_LEFT_KEY = KeyEvent.VK_LEFT;
    public static final int SHIP2_RIGHT_KEY = KeyEvent.VK_RIGHT;
    public static final int SHIP2_FIRE_KEY = KeyEvent.VK_SPACE;

    public static final int DEFAULT_LIVES = 3;

    public static final int EASY_MAX_SUBMARINES = 6;
    public static final int EASY_SPAWN_MIN_INTERVAL = 1600;
    public static final int EASY_SPAWN_MAX_INTERVAL = 4200;
    public static final int HARD_MAX_SUBMARINES = 14;
    public static final int HARD_SPAWN_MIN_INTERVAL = 700;
    public static final int HARD_SPAWN_MAX_INTERVAL = 3200;

    public static final int BOMB_WIDTH = 20;
    public static final int BOMB_HEIGHT = 20;
    public static final int DEFAULT_BOMB_SPEED = 8;
    public static final int FIRE_COOLDOWN = 500;

    public static final int SUBMARINE_WIDTH = 60;
    public static final int SUBMARINE_HEIGHT = 30;
    public static final int SUBMARINE_SPEED_MIN = 1;
    public static final int SUBMARINE_SPEED_MAX = 3;
    public static final int SUBMARINE_MAX_COUNT = 10;
    public static final int SUBMARINE_SPAWN_MIN_INTERVAL = 1000;
    public static final int SUBMARINE_SPAWN_MAX_INTERVAL = 5000;
    public static final int SUBMARINE_BULLET_WIDTH = 14;
    public static final int SUBMARINE_BULLET_HEIGHT = 14;
    public static final int SUBMARINE_BULLET_SPEED = 5;
    public static final int SUBMARINE_FIRE_COOLDOWN = 2000;
    public static final int SUBMARINE_TYPE_FRIENDLY = 0;
    public static final int SUBMARINE_TYPE_ENEMY = 1;
    public static final int SUBMARINE_TYPE_ELITE = 2;

    public static final int EXPLOSION_WIDTH = 60;
    public static final int EXPLOSION_HEIGHT = 60;
    public static final int FRAME_DELAY = 100;

    public static final String GAME_TITLE = "Submarine War";

    public static class Resources {
        public static final String BACKGROUND = "/images/background.png";
        public static final String SHIP_0 = "/images/ship0.png";
        public static final String SHIP_1 = "/images/ship1.png";
        public static final String SUBMARINE_Q1 = "/images/q1.png";
        public static final String SUBMARINE_Q2 = "/images/q2.png";
        public static final String SUBMARINE_R1 = "/images/r1.png";
        public static final String SUBMARINE_R2 = "/images/r2.png";
        public static final String SUBMARINE_H1 = "/images/h1.png";
        public static final String SUBMARINE_H2 = "/images/h2.png";
        public static final String BOMB = "/images/boom.png";
        public static final String ENEMY_BOMB = "/images/boom2.png";
        public static final String EXPLOSION_0 = "/images/b.png";
        public static final String EXPLOSION_1 = "/images/b1.png";
        public static final String EXPLOSION_2 = "/images/b2.png";
    }

    private Constants() {
        throw new AssertionError("Constants class cannot be instantiated");
    }
}

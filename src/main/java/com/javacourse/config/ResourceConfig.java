package com.javacourse.config;

import com.javacourse.framework.loader.ResourceManager;

public class ResourceConfig {
    public static void loadAllResources() {
        ResourceManager rm = ResourceManager.getInstance();

        rm.loadImage("background", Constants.Resources.BACKGROUND);
        rm.loadImage("ship0", Constants.Resources.SHIP_0);
        rm.loadImage("ship1", Constants.Resources.SHIP_1);
        rm.loadImage("boom", Constants.Resources.BOMB);
        rm.loadImage("q1", Constants.Resources.SUBMARINE_Q1);
        rm.loadImage("q2", Constants.Resources.SUBMARINE_Q2);
        rm.loadImage("r1", Constants.Resources.SUBMARINE_R1);
        rm.loadImage("r2", Constants.Resources.SUBMARINE_R2);
        rm.loadImage("h1", Constants.Resources.SUBMARINE_H1);
        rm.loadImage("h2", Constants.Resources.SUBMARINE_H2);
        rm.loadImage("b", Constants.Resources.EXPLOSION_0);
        rm.loadImage("b1", Constants.Resources.EXPLOSION_1);
        rm.loadImage("b2", Constants.Resources.EXPLOSION_2);
        rm.loadImage("boom2", Constants.Resources.ENEMY_BOMB);
    }

    private ResourceConfig() {
        throw new AssertionError();
    }
}

package com.javacourse.controller;

import com.javacourse.entity.Ship;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class PlayerController implements KeyListener {

    private Ship ship;
    private Set<Integer> pressedKeys;

    private int leftKey;
    private int rightKey;
    private int fireKey;

    private Runnable onFire;

    public PlayerController (Ship ship, int leftKey, int rightKey, int fireKey ) {
        this.ship = ship;
        this.leftKey = leftKey;
        this.rightKey = rightKey;
        this.fireKey = fireKey;
        this.pressedKeys = new HashSet<>();
    }

    public void setOnFire(Runnable onFire) {
        this.onFire = onFire;
    }
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys.add(key);

        if (key == leftKey) {
            ship.moveLeft();
        } else if (key == rightKey) {
            ship.moveRight();
        } else if (key == fireKey) {
            if (onFire != null) {
                onFire.run();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys.remove(key);

        if (key == leftKey || key == rightKey) {
            if (!pressedKeys.contains(leftKey) && !pressedKeys.contains(rightKey)) {
                ship.stopMoving();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }
}

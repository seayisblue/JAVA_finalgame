package com.javacourse.controller;

import com.javacourse.entity.Ship;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class PlayerController implements KeyListener {

    private Ship ship;
    private Set<Integer> pressedKeys;

    private int[] leftKeys;
    private int[] rightKeys;
    private int[] fireKeys;

    private Runnable onFire;

    public PlayerController(Ship ship, int[] leftKeys, int[] rightKeys, int[] fireKeys) {
        this.ship = ship;
        this.leftKeys = leftKeys;
        this.rightKeys = rightKeys;
        this.fireKeys = fireKeys;
        this.pressedKeys = new HashSet<>();
    }

    public void setOnFire(Runnable onFire) {
        this.onFire = onFire;
    }
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys.add(key);

        if (isInKeys(key, leftKeys)) {
            ship.moveLeft();
        } else if (isInKeys(key, rightKeys)) {
            ship.moveRight();
        } else if (isInKeys(key, fireKeys)) {
            if (onFire != null) {
                onFire.run();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys.remove(key);

        if (isInKeys(key, leftKeys) || isInKeys(key, rightKeys)) {
            if (!hasAnyPressed(leftKeys) && !hasAnyPressed(rightKeys)) {
                ship.stopMoving();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    private boolean isInKeys(int key, int[] keys) {
        if (keys == null) {
            return false;
        }
        for (int candidate : keys) {
            if (candidate == key) {
                return true;
            }
        }
        return false;
    }

    private boolean hasAnyPressed(int[] keys) {
        if (keys == null) {
            return false;
        }
        for (int candidate : keys) {
            if (pressedKeys.contains(candidate)) {
                return true;
            }
        }
        return false;
    }
}

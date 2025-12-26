package com.javacourse.framework.util;

public enum Direction {
    LEFT(-1,0),
    RIGHT(1,0),
    UP(0,-1),
    DOWN(0,1),
    NONE(0,0);

    private final int deltaX;
    private final int deltaY;

    Direction(int deltaX, int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }

    public Direction opposite() {
        switch(this) {
            case LEFT: return RIGHT;
            case RIGHT: return LEFT;
            case UP: return DOWN;
            case DOWN: return UP;
            default: return NONE;
        }
    }
}

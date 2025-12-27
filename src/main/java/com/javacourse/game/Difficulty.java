package com.javacourse.game;

public enum Difficulty {
    EASY("简单"),
    NORMAL("普通"),
    HARD("困难");

    private final String label;

    Difficulty(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

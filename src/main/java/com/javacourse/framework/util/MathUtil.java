package com.javacourse.framework.util;

import java.util.Random;

public class MathUtil {
    private static final Random RANDOM = new Random();

    public static int getRandomInt(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }

    public static double getRandomDouble(double min, double max) {
        return min + (max - min) * RANDOM.nextDouble();
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double getDistance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static boolean checkInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    private MathUtil() {
    }
}

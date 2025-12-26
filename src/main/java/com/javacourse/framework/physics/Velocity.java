package com.javacourse.framework.physics;

public class Velocity {
    private double vx;
    private double vy;

    public Velocity() {
        this.vx = 0;
        this.vy = 0;
    }

    public Velocity(double vx, double vy) {
        this.vx = vx;
        this.vy = vy;
    }

    public void add(Velocity other) {
        this.vx += other.vx;
        this.vy += other.vy;
    }

    public double getMagnitude() {
        return Math.sqrt(vx * vx + vy * vy);
    }

    public void normalize() {
        double magnitude = getMagnitude();
        if (magnitude != 0) {
            this.vx /= magnitude;
            this.vy /= magnitude;
        }
    }

    public void zero() {
        this.vx = 0;
        this.vy = 0;
    }


    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }


}

package com.javacourse.framework.core;

import com.javacourse.framework.physics.Bounds;
import com.javacourse.framework.physics.Velocity;
import com.javacourse.framework.render.SpriteSheet;

import java.awt.image.BufferedImage;
import java.util.List;

public abstract class GameEntity implements LifeCycle {
    protected double x;
    protected double y;
    protected int width;
    protected int height;
    protected boolean alive;
    protected Velocity velocity;
    protected Bounds bounds;
    protected SpriteSheet animation;

    public GameEntity(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.alive = true;
        this.velocity = new Velocity();
        this.bounds = new Bounds((int)x, (int)y, width, height);
    }

    protected void updatePosition() {
        this.x += velocity.getVx();
        this.y += velocity.getVy();
        this.bounds.setPosition((int)x, (int)y);
    }

    public boolean isOutOfBounds(int screenWidth, int screenHeight) {
        return x + width < 0 || x > screenWidth || y + height < 0 || y > screenHeight;
    }

    protected void initAnimation(List<BufferedImage> frames, int delay, boolean loop) {
        animation = new SpriteSheet(delay, loop);

        if (frames != null) {
            for (BufferedImage frame: frames) {
                if (frame != null) {
                    animation.addFrame(frame);
                }
            }
        }
    }

    @Override
    public boolean isAlive() {
        return alive;
    }
    @Override
    public void destroy() {
        this.alive = false;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public void setVelocity(Velocity velocity) {
        this.velocity = velocity;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }
}

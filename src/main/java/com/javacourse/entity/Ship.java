package com.javacourse.entity;

import com.javacourse.framework.core.GameEntity;
import com.javacourse.framework.render.Render;
import com.javacourse.config.Constants;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

public class Ship extends GameEntity implements Render {
    private int playerId;
    private int rightFrameIndex;
    private int leftFrameIndex;

    private long lastFireTime;
    private int fireCooldown; // milliseconds
    private boolean canFire;

    public Ship(int playerId, double x, double y, List<BufferedImage> frames) {
        super(x, y, Constants.SHIP_WIDTH, Constants.SHIP_HEIGHT);

        this.playerId = playerId;
        this.fireCooldown = Constants.FIRE_COOLDOWN;
        this.lastFireTime = 0;
        this.canFire = true;
        this.rightFrameIndex = 0;
        this.leftFrameIndex = 1;

        super.initAnimation(frames, Constants.FRAME_DELAY, true);
        if (animation != null) {
            animation.setPlaying(false);
            if (animation.getFrameCount() > 0) {
                animation.setCurrentFrameIndex(rightFrameIndex);
            }
        }
    }

    public Ship (int playerId, double x, double y, BufferedImage image) {
        this(playerId, x, y, Arrays.asList(image));
    }

    @Override
    public void update() {
        updatePosition();

        if (x < 0) {
            x = 0;
            velocity.setVx(0);
        }
        if (x + width > Constants.WINDOW_WIDTH) {
            x = Constants.WINDOW_WIDTH - width;
            velocity.setVx(0);
        }

        y = Constants.SEA_LEVEL - height;
        bounds.setPosition((int)x, (int)y);

        if (animation != null && animation.getFrameCount() > 0) {
            animation.update();
        }

        updateFireCooldown();
    }

    private void updateFireCooldown() {
        long currentTime = System.currentTimeMillis();
        if (!canFire && currentTime - lastFireTime >= fireCooldown) {
            canFire = true;
        }
    }

    public void moveLeft() {
        velocity.setVx(-Constants.SHIP_SPEED);
        setFrameIndex(leftFrameIndex);
    }

    public void moveRight() {
        velocity.setVx(Constants.SHIP_SPEED);
        setFrameIndex(rightFrameIndex);
    }


    public void stopMoving() {
        velocity.setVx(0);
    }

    public boolean tryFire() {
        if (!canFire) {
            return false;
        }

        canFire = false;
        lastFireTime = System.currentTimeMillis();
        return true;
    }

    public double getBombSpawnX() {
        return x + (width - Constants.BOMB_WIDTH) / 2.0;
    }

    public double getBombSpawnY() {
        return y;
    }

    @Override
    public void render(Graphics g) {
        if (animation != null && animation.getFrameCount() > 0) {
            BufferedImage currentFrame = animation.getCurrentFrame();
            g.drawImage(currentFrame, (int)x, (int)y, width, height, null);
        } else {
            g.setColor(playerId == 1 ? Color.BLUE : Color.GREEN);
            g.fillRect((int)x, (int)y, width, height);
            g.setColor(Color.WHITE);
            g.drawRect((int)x, (int)y, width, height);
            g.drawString("P" + playerId, (int)x + width/2 - 5, (int)y + height/2);
        }

    }

    public int getPlayerId() {
        return playerId;
    }

    public boolean isCanFire() {
        return canFire;
    }

    private void setFrameIndex(int index) {
        if (animation == null) {
            return;
        }
        if (index >= 0 && index < animation.getFrameCount()) {
            animation.setCurrentFrameIndex(index);
        }
    }
}

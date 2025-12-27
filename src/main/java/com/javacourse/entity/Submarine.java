package com.javacourse.entity;

import com.javacourse.framework.core.GameEntity;
import com.javacourse.framework.render.Render;
import com.javacourse.config.Constants;
import com.javacourse.framework.util.Direction;
import com.javacourse.framework.util.MathUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

public class Submarine extends GameEntity implements Render {
    private static int enemySpeedMin = Constants.SUBMARINE_SPEED_MIN;
    private static int enemySpeedMax = Constants.SUBMARINE_SPEED_MAX;
    private static int eliteFireCooldown = Constants.SUBMARINE_FIRE_COOLDOWN;

    private Direction direction;
    private int submarineType;
    private int score;
    private int rightFrameIndex;
    private int leftFrameIndex;
    private long lastFireTime;
    private int fireCooldown;
    private boolean canFire;

    public Submarine (double x, double y, Direction direction, int submarineType, List<BufferedImage> frames) {
        super(x, y, Constants.SUBMARINE_WIDTH, Constants.SUBMARINE_HEIGHT);

        this.direction = direction;
        this.submarineType = submarineType;
        //TODO: 解耦游戏计分方式
        this.score = 10 + submarineType*5;
        this.rightFrameIndex = 0;
        this.leftFrameIndex = 1;
        this.fireCooldown = eliteFireCooldown;
        this.lastFireTime = 0;
        this.canFire = true;

        double speed;
        if (submarineType == Constants.SUBMARINE_TYPE_FRIENDLY) {
            speed = MathUtil.getRandomInt(Constants.SUBMARINE_SPEED_MIN, Constants.SUBMARINE_SPEED_MAX);
        } else {
            speed = MathUtil.getRandomInt(enemySpeedMin, enemySpeedMax);
        }
        if (direction == Direction.LEFT) {
            this.velocity.setVx(-speed);
        } else {
            this.velocity.setVx(speed);
        }

        initAnimation(frames, Constants.FRAME_DELAY, true);
        if (animation != null) {
            animation.setPlaying(false);
            setFrameIndex(direction == Direction.LEFT ? leftFrameIndex : rightFrameIndex);
        }
    }

    public Submarine (double x, double y, Direction direction, int submarineType, BufferedImage image) {
        this(x, y, direction, submarineType, Arrays.asList(image));
    }

    @Override
    public void update() {
        updatePosition();
        if (isOutOfBounds(Constants.MAP_WIDTH, Constants.MAP_HEIGHT)) {
            destroy();
        }
        bounds.setPosition((int)x, (int)y);
        updateFireCooldown();
    }

    @Override
    public void render (Graphics g) {
        if (animation != null && animation.getFrameCount() > 0) {
            BufferedImage currentFrame = animation.getCurrentFrame();
            g.drawImage(currentFrame, (int)x, (int)y, width, height, null);
        } else {
            Color[] colors = {
                    Color.CYAN,
                    Color.MAGENTA,
                    Color.ORANGE,
                    Color.PINK
            };
            g.setColor(colors[submarineType]);
            g.fillRect((int)x, (int)y, width, height);
            g.setColor(Color.WHITE);
            g.drawRect((int)x, (int)y, width, height);
        }
    }

    public int getScore() {
        return score;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getSubmarineType() {
        return submarineType;
    }

    public boolean canFire() {
        return canFire;
    }

    public void markFired() {
        canFire = false;
        lastFireTime = System.currentTimeMillis();
    }

    public static void setEnemySpeedRange(int min, int max) {
        enemySpeedMin = min;
        enemySpeedMax = max;
    }

    public static void setEliteFireCooldown(int cooldown) {
        eliteFireCooldown = cooldown;
    }

    private void updateFireCooldown() {
        if (submarineType != Constants.SUBMARINE_TYPE_ELITE) {
            return;
        }
        long currentTime = System.currentTimeMillis();
        if (!canFire && currentTime - lastFireTime >= fireCooldown) {
            canFire = true;
        }
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

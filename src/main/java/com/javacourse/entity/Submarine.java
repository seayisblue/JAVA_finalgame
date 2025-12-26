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

    private BufferedImage image;
    private Direction direction;
    private int submarineType;
    private int score;

    public Submarine (double x, double y, Direction direction, int submarineType, List<BufferedImage> frames) {
        super(x, y, Constants.SUBMARINE_WIDTH, Constants.SUBMARINE_HEIGHT);

        this.direction = direction;
        this.submarineType = submarineType;
        //TODO: 解耦游戏计分方式
        this.score = 10 + submarineType*5;

        double speed = MathUtil.getRandomInt(Constants.SUBMARINE_SPEED_MIN, Constants.SUBMARINE_SPEED_MAX);
        if (direction == Direction.LEFT) {
            this.velocity.setVx(-speed);
        } else {
            this.velocity.setVx(speed);
        }

        initAnimation(frames, Constants.FRAME_DELAY, true);
    }

    public Submarine (double x, double y, Direction direction, int submarineType, BufferedImage image) {
        this(x, y, direction, submarineType, Arrays.asList(image));
        this.image = image;
    }

    @Override
    public void update() {
        updatePosition();
        if (isOutOfBounds(Constants.MAP_WIDTH, Constants.MAP_HEIGHT)) {
            destroy();
        }
        bounds.setPosition((int)x, (int)y);
    }

    @Override
    public void render (Graphics g) {
        if (image != null) {
            g.drawImage(image, (int)x, (int)y, width, height, null);
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
}

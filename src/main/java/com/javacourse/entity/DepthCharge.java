package com.javacourse.entity;

import com.javacourse.framework.core.GameEntity;
import com.javacourse.framework.render.Render;
import com.javacourse.config.Constants;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

public class DepthCharge extends GameEntity implements Render {

    private int ownerId;
    private BufferedImage image;

    public DepthCharge(double x, double y, int ownerId, List<BufferedImage> frames) {
        super(x, y, Constants.BOMB_WIDTH, Constants.BOMB_HEIGHT);
        this.ownerId = ownerId;
        this.velocity.setVy(Constants.DEFAULT_BOMB_SPEED);

        initAnimation(frames, Constants.FRAME_DELAY, true);
    }

    public DepthCharge(double x, double y, int ownerId, BufferedImage image) {
        this(x, y, ownerId, Arrays.asList(image));
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
    public void render(Graphics g) {
        if (image != null) {
            g.drawImage(image, (int)x, (int)y, width, height, null);
        } else {
            g.setColor(java.awt.Color.RED);
            g.fillOval((int)x, (int)y, width, height);
            g.setColor(java.awt.Color.YELLOW);
            g.drawOval((int)x, (int)y, width, height);
        }
    }

    public int getOwnerId() {
        return ownerId;
    }

}

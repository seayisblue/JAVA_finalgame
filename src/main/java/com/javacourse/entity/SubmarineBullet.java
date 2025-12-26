package com.javacourse.entity;

import com.javacourse.config.Constants;
import com.javacourse.framework.core.GameEntity;
import com.javacourse.framework.render.Render;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class SubmarineBullet extends GameEntity implements Render {
    private BufferedImage image;

    public SubmarineBullet(double x, double y, double targetX, double targetY, BufferedImage image) {
        super(x, y, Constants.SUBMARINE_BULLET_WIDTH, Constants.SUBMARINE_BULLET_HEIGHT);
        this.image = image;
        setVelocityTowards(targetX, targetY);
    }

    @Override
    public void update() {
        updatePosition();
        if (isOutOfBounds(Constants.MAP_WIDTH, Constants.MAP_HEIGHT)) {
            destroy();
        }
        bounds.setPosition((int) x, (int) y);
    }

    @Override
    public void render(Graphics g) {
        if (image != null) {
            g.drawImage(image, (int) x, (int) y, width, height, null);
        } else {
            g.setColor(java.awt.Color.ORANGE);
            g.fillOval((int) x, (int) y, width, height);
        }
    }

    private void setVelocityTowards(double targetX, double targetY) {
        double dx = targetX - x;
        double dy = targetY - y;
        double length = Math.sqrt(dx * dx + dy * dy);
        if (length == 0) {
            velocity.setVy(Constants.SUBMARINE_BULLET_SPEED);
            return;
        }
        velocity.setVx(dx / length * Constants.SUBMARINE_BULLET_SPEED);
        velocity.setVy(dy / length * Constants.SUBMARINE_BULLET_SPEED);
    }
}

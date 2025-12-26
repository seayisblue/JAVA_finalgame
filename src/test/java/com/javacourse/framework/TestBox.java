package com.javacourse.framework;

import com.javacourse.framework.core.GameEntity;
import com.javacourse.config.Constants;

import java.awt.*;

public class TestBox extends GameEntity {
    private Color color;
    private static final double SPEED = 5.0;

    public TestBox(double x, double y, int width, int height) {
        super(x, y, width, height);
        this.color = new Color(0, 201, 167);
    }

    @Override
    public void update() {
        updatePosition();
        if (x < 0) x = 0;
        if (x + width > Constants.WINDOW_WIDTH) {
            x = Constants.WINDOW_WIDTH - width;
        }
        if (y < 0) y = 0;
        if (y + height > Constants.WINDOW_HEIGHT) {
            y = Constants.WINDOW_HEIGHT - height;
        }

        bounds.setPosition((int)x, (int)y);
    }

    public void render(Graphics2D graphics2D) {
        graphics2D.setColor(color);
        graphics2D.fillRect((int)x, (int)y, width, height);

        graphics2D.setColor(Color.WHITE);
        graphics2D.drawRect((int)x, (int)y, width, height);

        graphics2D.setColor(Color.RED);
        graphics2D.fillOval((int)x + width/2 - 3, (int)y + height/2 - 3, 6, 6);

        graphics2D.setColor(Color.RED);
        graphics2D.setFont(new Font("Arial", Font.PLAIN, 10));
        graphics2D.drawString(String.format("(%.0f, %.0f)", x, y), (int)x, (int)y - 5);
    }

    public void moveLeft() {
        velocity.setVx(-SPEED);
    }
    public void moveRight() {
        velocity.setVx(SPEED);
    }
    public void moveUp() {
        velocity.setVy(-SPEED);
    }
    public void moveDown() {
        velocity.setVy(SPEED);
    }

    public void stopHorizontal() {
        velocity.setVx(0);
    }
    public void stopVertical() {
        velocity.setVy(0);
    }
}

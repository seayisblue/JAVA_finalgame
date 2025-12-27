package com.javacourse.entity;

import com.javacourse.framework.core.GameEntity;
import com.javacourse.framework.render.Render;
import com.javacourse.framework.render.SpriteSheet;
import com.javacourse.config.Constants;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Explosion extends GameEntity implements Render {

    public Explosion(double x, double y, List<BufferedImage> frames) {
        super(x, y, Constants.EXPLOSION_WIDTH, Constants.EXPLOSION_HEIGHT);
        initAnimation(frames, Constants.FRAME_DELAY, false);
    }

    @Override
    public void update() {

        if (animation != null && animation.getFrameCount() > 0) {
            animation.update();

            if (animation.isFinished()) {
                destroy();
            }
        } else {
            destroy();
        }
    }

    @Override
    public void render(Graphics g) {

        if (animation != null && animation.getCurrentFrame() != null) {
            BufferedImage currentFrame = animation.getCurrentFrame();
            g.drawImage(currentFrame, (int)x, (int)y, width, height, null);
        } else {
            g.setColor(java.awt.Color.ORANGE);
            g.fillOval((int)x, (int)y, width, height);
            g.setColor(java.awt.Color.RED);
            g.fillOval((int)x + 10, (int)y + 10, width - 20, height - 20);
            g.setColor(java.awt.Color.YELLOW);
            g.fillOval((int)x + 20, (int)y + 20, width - 40, height - 40);
        }
    }
}

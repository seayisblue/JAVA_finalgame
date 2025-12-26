package com.javacourse.framework;

import com.javacourse.framework.core.GameWorld;
import com.javacourse.framework.core.LifeCycle;
import com.javacourse.framework.loader.ResourceManager;
import com.javacourse.framework.render.GameCanvas;
import com.javacourse.config.Constants;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TestCanvas extends GameCanvas {
    private GameWorld world;
    private BufferedImage backgroundImage;

    public TestCanvas(GameWorld world) {
        super(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        this.world = world;

        // 加载背景图片
        ResourceManager resourceManager = ResourceManager.getInstance();
        this.backgroundImage = resourceManager.getImage("background");

        if (backgroundImage == null) {
            System.err.println("⚠️ Background image not loaded!");
        }
    }

    @Override
    protected void render(Graphics2D g) {
        // 绘制背景图片
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, width, height, null);
        } else {
            // 后备方案
            g.setColor(new Color(135, 206, 250));
            g.fillRect(0, 0, width, Constants.SEA_LEVEL);
            g.setColor(new Color(0, 105, 148));
            g.fillRect(0, Constants.SEA_LEVEL, width, height - Constants.SEA_LEVEL);
        }

        // 绘制坐标网格（半透明白色）
        g.setColor(new Color(255, 255, 255, 80));
        g.setFont(new Font("Monospaced", Font.PLAIN, 10));
        for (int i = 0; i < width; i += 50) {
            g.drawLine(i, 0, i, height);
            if (i % 100 == 0) {
                g.drawString("" + i, i + 2, 12);
            }
        }
        for (int i = 0; i < height; i += 50) {
            g.drawLine(0, i, width, i);
            if (i % 100 == 0) {
                g.drawString("" + i, 2, i + 12);
            }
        }

        // 绘制 Constants.SEA_LEVEL 参考线（红色）
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(2));
        g.drawLine(0, Constants.SEA_LEVEL, width, Constants.SEA_LEVEL);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Constants.SEA_LEVEL = " + Constants.SEA_LEVEL, 10, Constants.SEA_LEVEL - 8);

        // 绘制所有游戏对象
        for (LifeCycle entity : world.getEntities()) {
            if (entity instanceof TestBox) {
                TestBox box = (TestBox) entity;
                box.render(g);
            }
        }

        // 绘制信息
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("FPS: 60", 10, 30);
        g.drawString("Entity Count: " + world.getEntities().size(), 10, 50);
        g.drawString("Use Arrow Keys to move box", 10, height - 20);
        g.drawString("Find the actual sea level in background image!", 10, height - 40);
    }
}



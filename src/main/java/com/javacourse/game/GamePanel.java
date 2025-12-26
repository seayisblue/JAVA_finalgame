package com.javacourse.game;

import com.javacourse.framework.core.GameWorld;
import com.javacourse.framework.core.LifeCycle;
import com.javacourse.framework.loader.ResourceManager;
import com.javacourse.framework.render.GameCanvas;
import com.javacourse.framework.render.Render;
import com.javacourse.config.Constants;
import com.javacourse.system.ScoreSystem;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends GameCanvas {
    private GameWorld world;
    private ScoreSystem scoreSystem;
    private BufferedImage backgroundImage;

    public GamePanel (GameWorld world, ScoreSystem scoreSystem) {
        super(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        this.world = world;
        this.scoreSystem = scoreSystem;

        ResourceManager resourceManager = ResourceManager.getInstance();
        this.backgroundImage = resourceManager.getImage("background");
    }

    @Override
    protected void render(Graphics2D g) {
        renderBackground(g);
        renderEntities(g);
        renderUI(g);
    }

    private void renderBackground(Graphics2D g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, width, height, null);
        } else {
            // 天空（浅蓝色）
            g.setColor(new Color(135, 206, 250));
            g.fillRect(0, 0, width, Constants.SEA_LEVEL);

            // 海面线
            g.setColor(new Color(70, 130, 180));
            g.setStroke(new BasicStroke(2));
            g.drawLine(0, Constants.SEA_LEVEL, width, Constants.SEA_LEVEL);

            // 海水（深蓝色）
            g.setColor(new Color(0, 105, 148));
            g.fillRect(0, Constants.SEA_LEVEL, width, height - Constants.SEA_LEVEL);
        }
    }

    private void renderEntities(Graphics2D g) {
        for (LifeCycle entity : world.getEntities()) {
            if (entity instanceof Render) {
                //因为之前我们的语句导致了类型是LifeCycle，所以这里需要强制转换
                ((Render) entity).render(g);
            }
        }
    }

    private void renderUI(Graphics2D g) {
        g.setFont(new Font("Arial", Font.BOLD, 24));

        g.setColor(Color.BLUE);
        g.drawString("P1: " + scoreSystem.getPlayer1Score(), 20, 40);

        g.setColor(Color.GREEN);
        String p2Score = "P2: " + scoreSystem.getPlayer2Score();
        int p2Width = g.getFontMetrics().stringWidth(p2Score);
        g.drawString(p2Score, width - p2Width - 20, 40);

    }


}

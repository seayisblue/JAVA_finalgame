package com.javacourse.game;

import com.javacourse.config.Constants;
import com.javacourse.framework.core.GameWorld;
import com.javacourse.framework.core.LifeCycle;
import com.javacourse.framework.loader.ResourceManager;
import com.javacourse.framework.render.GameCanvas;
import com.javacourse.framework.render.Render;
import com.javacourse.system.LifeSystem;
import com.javacourse.system.ScoreSystem;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GamePanel extends GameCanvas {
    private final GameWorld world;
    private final ScoreSystem scoreSystem;
    private final LifeSystem lifeSystem;
    private final GameUIController uiController;
    private BufferedImage backgroundImage;
    private Point mousePosition;


    private UiButton startButton;
    private UiButton singleModeButton;
    private UiButton doubleModeButton;
    private UiButton easyButton;
    private UiButton normalButton;
    private UiButton hardButton;
    private UiButton resumeButton;
    private UiButton exitButton;
    private UiButton restartButton;
    private UiButton menuButton;
    public GamePanel(GameWorld world, ScoreSystem scoreSystem, LifeSystem lifeSystem, GameUIController uiController) {
        super(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        this.world = world;
        this.scoreSystem = scoreSystem;
        this.lifeSystem = lifeSystem;
        this.uiController = uiController;
        this.mousePosition = new Point(-1, -1);

        ResourceManager resourceManager = ResourceManager.getInstance();
        this.backgroundImage = resourceManager.getImage("background");

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getPoint());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mousePosition = new Point(-1, -1);
                repaint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mousePosition = e.getPoint();
                repaint();
            }
        });
    }

    @Override
    protected void render(Graphics2D g) {
        renderBackground(g);
        renderEntities(g);
        renderUI(g);
        renderOverlay(g);
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
        if (uiController.getGameState() == GameState.START) {
            return;
        }
        for (LifeCycle entity : world.getEntities()) {
            if (entity instanceof Render) {
                //因为之前我们的语句导致了类型是LifeCycle，所以这里需要强制转换
                ((Render) entity).render(g);
            }
        }
    }

    private void renderUI(Graphics2D g) {
        if (uiController.getGameState() != GameState.RUNNING) {
            return;
        }
        g.setFont(new Font("Arial", Font.BOLD, 22));


        g.setColor(new Color(10, 30, 120));
        String p1Score = "P1: " + scoreSystem.getPlayer1Score() + "  生命: " + lifeSystem.getPlayer1Lives();
        g.drawString(p1Score, 20, 40);

        if (uiController.getGameMode() == GameMode.DOUBLE) {
            g.setColor(new Color(0, 120, 80));
            String p2Score = "P2: " + scoreSystem.getPlayer2Score() + "  生命: " + lifeSystem.getPlayer2Lives();
            int p2Width = g.getFontMetrics().stringWidth(p2Score);
            g.drawString(p2Score, width - p2Width - 20, 40);
        }
    }

    private void renderOverlay(Graphics2D g) {
        updateLayout();
        GameState state = uiController.getGameState();
        if (state == GameState.START) {
            renderStartMenu(g);
        } else if (state == GameState.PAUSED) {
            renderPauseMenu(g);
        } else if (state == GameState.GAME_OVER) {
            renderGameOverMenu(g);
        }
    }

    private void renderStartMenu(Graphics2D g) {
        drawPanelBackground(g);
        int panelWidth = (int) (width * 0.7);
        int panelHeight = (int) (height * 0.8);
        int panelX = (width - panelWidth) / 2;
        int panelY = (height - panelHeight) / 2;


        g.setFont(new Font("Serif", Font.BOLD, 52));


        g.setColor(new Color(235, 245, 255));
        drawCenteredString(g, "深海潜艇大战", new Rectangle(panelX, panelY, panelWidth, panelHeight / 4));

        drawPrimaryButton(g, startButton, "开始游戏");


        g.setFont(new Font("Arial", Font.PLAIN, 20));

        g.setColor(Color.WHITE);
        g.drawString("模式选择", panelX + 60, startButton.bounds.y + startButton.bounds.height + 45);
        drawSelectionButton(g, singleModeButton, "单人模式", uiController.getGameMode() == GameMode.SINGLE);
        drawSelectionButton(g, doubleModeButton, "双人模式", uiController.getGameMode() == GameMode.DOUBLE);

        g.drawString("难度选择", panelX + 60, singleModeButton.bounds.y + singleModeButton.bounds.height + 45);
        drawSelectionButton(g, easyButton, Difficulty.EASY.getLabel(), uiController.getDifficulty() == Difficulty.EASY);
        drawSelectionButton(g, normalButton, Difficulty.NORMAL.getLabel(), uiController.getDifficulty() == Difficulty.NORMAL);
        drawSelectionButton(g, hardButton, Difficulty.HARD.getLabel(), uiController.getDifficulty() == Difficulty.HARD);

        g.setFont(new Font("Arial", Font.PLAIN, 16));

        g.setColor(new Color(220, 235, 255));
        int instructionY = easyButton.bounds.y + easyButton.bounds.height + 30;
        g.drawString("操作说明：", panelX + 60, instructionY);
        g.drawString("• 移动：方向键↑↓←→或 WASD 控制玩家船只", panelX + 80, instructionY + 24);
        g.drawString("• 射击：空格键发射子弹", panelX + 80, instructionY + 48);
        g.drawString("• 暂停：ESC 键暂停游戏", panelX + 80, instructionY + 72);
    }

    private void renderPauseMenu(Graphics2D g) {
        drawOverlayMask(g);
        drawPanelBackground(g);

        g.setFont(new Font("Arial", Font.BOLD, 32));

        g.setColor(Color.WHITE);
        drawCenteredString(g, "游戏暂停", new Rectangle(0, height / 2 - 140, width, 50));

        drawPrimaryButton(g, resumeButton, "继续游戏");
        drawPrimaryButton(g, exitButton, "退出游戏");
    }

    private void renderGameOverMenu(Graphics2D g) {
        drawOverlayMask(g);
        drawPanelBackground(g);

        g.setFont(new Font("Serif", Font.BOLD, 46));
        g.setColor(new Color(255, 120, 60));
        drawCenteredString(g, "游戏结束", new Rectangle(0, height / 2 - 170, width, 60));

        g.setFont(new Font("Arial", Font.BOLD, 20));

        g.setColor(Color.WHITE);
        if (uiController.getGameMode() == GameMode.SINGLE) {
            int score = scoreSystem.getPlayer1Score();
            drawCenteredString(g, "你的分数：" + score, new Rectangle(0, height / 2 - 100, width, 30));
            String recordText = "历史最高分：" + uiController.getSingleHighScore();
            if (uiController.isNewRecord()) {
                recordText += "  新纪录！";
            }
            drawCenteredString(g, recordText, new Rectangle(0, height / 2 - 70, width, 30));
        } else {
            int p1 = scoreSystem.getPlayer1Score();
            int p2 = scoreSystem.getPlayer2Score();
            int total = p1 + p2;
            drawCenteredString(g, "玩家1：" + p1 + "    玩家2：" + p2 + "    总得分：" + total,
                    new Rectangle(0, height / 2 - 100, width, 30));
            String recordText = "历史最高总分：" + uiController.getDoubleHighScore();
            if (uiController.isNewRecord()) {
                recordText += "  新纪录！";
            }
            drawCenteredString(g, recordText, new Rectangle(0, height / 2 - 70, width, 30));
        }

        drawPrimaryButton(g, restartButton, "重新开始");
        drawPrimaryButton(g, menuButton, "返回主菜单");
    }

    private void drawPanelBackground(Graphics2D g) {
        int panelWidth = (int) (width * 0.72);
        int panelHeight = (int) (height * 0.78);
        int panelX = (width - panelWidth) / 2;
        int panelY = (height - panelHeight) / 2;
        g.setColor(new Color(10, 30, 80, 200));
        g.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 30, 30);
        g.setColor(new Color(150, 200, 255, 120));
        g.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 30, 30);
    }

    private void drawOverlayMask(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 140));
        g.fillRect(0, 0, width, height);
    }

    private void drawPrimaryButton(Graphics2D g, UiButton button, String label) {
        if (button == null) {
            return;
        }
        boolean hover = button.bounds.contains(mousePosition);
        Color baseColor = new Color(12, 46, 120);
        Color hoverColor = new Color(70, 140, 220);
        g.setColor(hover ? hoverColor : baseColor);
        g.fillRoundRect(button.bounds.x, button.bounds.y, button.bounds.width, button.bounds.height, 16, 16);
        g.setColor(Color.WHITE);
        g.drawRoundRect(button.bounds.x, button.bounds.y, button.bounds.width, button.bounds.height, 16, 16);

        g.setFont(new Font("Arial", Font.BOLD, 20));

        drawCenteredString(g, label, button.bounds);
    }

    private void drawSelectionButton(Graphics2D g, UiButton button, String label, boolean selected) {
        if (button == null) {
            return;
        }
        boolean hover = button.bounds.contains(mousePosition);
        Color fillColor = selected ? new Color(80, 140, 220) : new Color(20, 60, 120);
        if (hover) {
            fillColor = fillColor.brighter();
        }
        g.setColor(fillColor);
        g.fillRoundRect(button.bounds.x, button.bounds.y, button.bounds.width, button.bounds.height, 12, 12);
        g.setColor(new Color(220, 235, 255));
        g.drawRoundRect(button.bounds.x, button.bounds.y, button.bounds.width, button.bounds.height, 12, 12);

        g.setFont(new Font("Arial", Font.PLAIN, 18));

        drawCenteredString(g, label, button.bounds);
    }

    private void handleClick(Point point) {
        updateLayout();
        GameState state = uiController.getGameState();
        if (state == GameState.START) {
            if (startButton != null && startButton.bounds.contains(point)) {
                uiController.startGame();
            } else if (singleModeButton != null && singleModeButton.bounds.contains(point)) {
                uiController.setGameMode(GameMode.SINGLE);
            } else if (doubleModeButton != null && doubleModeButton.bounds.contains(point)) {
                uiController.setGameMode(GameMode.DOUBLE);
            } else if (easyButton != null && easyButton.bounds.contains(point)) {
                uiController.setDifficulty(Difficulty.EASY);
            } else if (normalButton != null && normalButton.bounds.contains(point)) {
                uiController.setDifficulty(Difficulty.NORMAL);
            } else if (hardButton != null && hardButton.bounds.contains(point)) {
                uiController.setDifficulty(Difficulty.HARD);
            }
        } else if (state == GameState.PAUSED) {
            if (resumeButton != null && resumeButton.bounds.contains(point)) {
                uiController.resumeGame();
            } else if (exitButton != null && exitButton.bounds.contains(point)) {
                uiController.exitToMenu();
            }
        } else if (state == GameState.GAME_OVER) {
            if (restartButton != null && restartButton.bounds.contains(point)) {
                uiController.restartGame();
            } else if (menuButton != null && menuButton.bounds.contains(point)) {
                uiController.exitToMenu();
            }
        }
    }

    private void updateLayout() {
        int centerX = width / 2;
        int panelWidth = (int) (width * 0.7);
        int panelHeight = (int) (height * 0.8);
        int panelX = (width - panelWidth) / 2;
        int panelY = (height - panelHeight) / 2;
        int buttonWidth = 220;
        int buttonHeight = 50;
        int startButtonY = panelY + panelHeight / 2 - 120;

        startButton = new UiButton(new Rectangle(centerX - buttonWidth / 2, startButtonY, buttonWidth, buttonHeight));
        resumeButton = new UiButton(new Rectangle(centerX - buttonWidth / 2, height / 2 - 20, buttonWidth, buttonHeight));
        exitButton = new UiButton(new Rectangle(centerX - buttonWidth / 2, height / 2 + 50, buttonWidth, buttonHeight));
        restartButton = new UiButton(new Rectangle(centerX - buttonWidth / 2, height / 2 + 10, buttonWidth, buttonHeight));
        menuButton = new UiButton(new Rectangle(centerX - buttonWidth / 2, height / 2 + 80, buttonWidth, buttonHeight));

        int modeButtonWidth = 150;
        int modeButtonHeight = 40;
        int modeStartY = startButtonY + buttonHeight + 40;
        singleModeButton = new UiButton(new Rectangle(panelX + 60, modeStartY + 20, modeButtonWidth, modeButtonHeight));
        doubleModeButton = new UiButton(new Rectangle(panelX + 60 + modeButtonWidth + 20, modeStartY + 20, modeButtonWidth, modeButtonHeight));

        int diffButtonWidth = 110;
        int diffButtonHeight = 36;
        int diffStartY = modeStartY + modeButtonHeight + 50;
        easyButton = new UiButton(new Rectangle(panelX + 60, diffStartY + 20, diffButtonWidth, diffButtonHeight));
        normalButton = new UiButton(new Rectangle(panelX + 60 + diffButtonWidth + 16, diffStartY + 20, diffButtonWidth, diffButtonHeight));
        hardButton = new UiButton(new Rectangle(panelX + 60 + (diffButtonWidth + 16) * 2, diffStartY + 20, diffButtonWidth, diffButtonHeight));
    }

    private void drawCenteredString(Graphics2D g, String text, Rectangle rect) {
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.drawString(text, x, y);
    }


    private static class UiButton {
        private final Rectangle bounds;

        private UiButton(Rectangle bounds) {
            this.bounds = bounds;
        }
    }


}

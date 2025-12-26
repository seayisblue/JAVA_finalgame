package com.javacourse.framework.render;

import javax.swing.*;
import java.awt.*;

public abstract class GameCanvas extends JPanel {
    protected int width;
    protected int height;

    public GameCanvas(int width, int height) {
        this.width = width;
        this.height = height;

        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        setFocusable(true);
    }

    protected abstract void render(Graphics2D g2d);

    public void refresh() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        render(g2d);
    }


    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}

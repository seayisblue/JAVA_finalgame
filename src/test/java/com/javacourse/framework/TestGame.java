package com.javacourse.framework;

import com.javacourse.config.ResourceConfig;
import com.javacourse.framework.core.GameLoop;
import com.javacourse.framework.core.GameWorld;
import com.javacourse.framework.render.GameCanvas;
import com.javacourse.config.Constants;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TestGame {
    private GameWorld world;
    private GameLoop gameLoop;
    private TestCanvas canvas;
    private TestBox box;

    public TestGame() {
        // 初始化资源管理器
        ResourceConfig.loadAllResources();

        world = new GameWorld(Constants.WINDOW_WIDTH,
                Constants.WINDOW_HEIGHT);
        canvas = new TestCanvas(world);
        box = new TestBox(400, 300, 50, 50);
        world.addEntity(box);

        gameLoop = new GameLoop(world, ()->canvas.refresh(), 60);

        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> box.moveLeft();
                    case KeyEvent.VK_RIGHT -> box.moveRight();
                    case KeyEvent.VK_UP -> box.moveUp();
                    case KeyEvent.VK_DOWN -> box.moveDown();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT ||
                        e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    box.stopHorizontal();
                }
                if (e.getKeyCode() == KeyEvent.VK_UP ||
                        e.getKeyCode() == KeyEvent.VK_DOWN) {
                    box.stopVertical();
                }
            }
        });
    }


    public void start() {
        gameLoop.start();
    }

    public void stop() {
        gameLoop.stop();
    }

    public GameCanvas getCanvas() {
        return canvas;
    }
}

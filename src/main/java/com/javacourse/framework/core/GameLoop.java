package com.javacourse.framework.core;

public class GameLoop implements Runnable{
    private GameWorld world;
    private Runnable renderCallback;
    private Thread gameThread;

    private boolean running;
    private int targetFPS;
    private long targetFrameTime;

    private int currentFPS;
    private long frameCount;

    public GameLoop(GameWorld world, Runnable renderCallback, int targetFPS) {
        this.world = world;
        this.renderCallback = renderCallback;
        this.targetFPS = targetFPS;
        this.targetFrameTime = 1_000_000_000L / targetFPS;
        this.running = false;
        this.currentFPS = 0;
        this.frameCount = 0;
    }

    public void start() {
        if (running) {
            return;
        }
        running = true;
        world.start();
        gameThread = new Thread(this, "GameLoop-Thread");
        gameThread.start();
    }

    public void stop() {
        running = false;
        world.stop();
        try {
            if (gameThread != null) {
                gameThread.join();
            }
        } catch (InterruptedException e) {
            System.err.println("Failed to stop game thread: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long lastFPSTime = System.currentTimeMillis();
        int frames = 0;

        while (running) {
            long currentTime = System.nanoTime();
            long elapsed = currentTime - lastTime;

            world.update();
            if (renderCallback != null) {
                renderCallback.run();
            }

            frames ++; frameCount++;

            if (System.currentTimeMillis() - lastFPSTime >= 1000) {
                currentFPS = frames;
                frames = 0;
                lastFPSTime += 1000;
            }

            long frameTime = System.nanoTime() - currentTime;
            long sleepTime = targetFrameTime - frameTime;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime / 1_000_000,(int)(sleepTime % 1_000_000));
                } catch (InterruptedException e) {
                    System.err.println("Game loop sleep interrupted: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            lastTime = currentTime;
        }
    }

    public void setTargetFPS(int targetFPS) {
        this.targetFPS = targetFPS;
        this.targetFrameTime = 1_000_000_000L / targetFPS;
    }

    public boolean isRunning() {
        return running;
    }

    public int getCurrentFPS() {
        return currentFPS;
    }

    public long getFrameCount() {
        return frameCount;
    }

    public GameWorld getWorld() {
        return world;
    }

    public int getTargetFPS() {
        return targetFPS;
    }
}

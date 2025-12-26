package com.javacourse.framework.render;

import com.javacourse.framework.loader.ImageLoader;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SpriteSheet {
    private List<BufferedImage> frames;
    private int currentFrameIndex;
    private int frameDelay;
    private long lastFrameTime;
    private boolean loop;
    private boolean playing;

    public SpriteSheet(int frameDelay, boolean loop) {
        this.frames = new ArrayList<>();
        this.frameDelay = frameDelay;
        this.loop = loop;
        this.playing = true;
        this.currentFrameIndex = 0;
        this.lastFrameTime = System.currentTimeMillis();
    }

    public void addFrame(BufferedImage frame) {
        frames.add(frame);
    }

    public void addFrame(String imagePath) {
        BufferedImage frame = ImageLoader.loadImage(imagePath);
        if (frame != null) {
            frames.add(frame);
        }
    }

    public void update() {
        if (!playing || frames.isEmpty()) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameTime >= frameDelay) {
            currentFrameIndex ++;
            if (currentFrameIndex >= frames.size()) {
                if (loop) {
                    currentFrameIndex = 0;
                } else {
                    currentFrameIndex = frames.size() - 1;
                    playing = false;
                }
            }
            lastFrameTime = currentTime;
        }

    }

    public BufferedImage getCurrentFrame() {
        if (frames.isEmpty()) {
            return null;
        }
        return frames.get(currentFrameIndex);
    }

    public void reset() {
        currentFrameIndex = 0;
        playing = true;
        lastFrameTime = System.currentTimeMillis();
    }

    public boolean isFinished() {
        return !loop && currentFrameIndex >= frames.size() - 1;
    }

    public int getFrameCount() {
        return frames.size();
    }



    public List<BufferedImage> getFrames() {
        return frames;
    }

    public void setFrames(List<BufferedImage> frames) {
        this.frames = frames;
    }

    public int getCurrentFrameIndex() {
        return currentFrameIndex;
    }

    public void setCurrentFrameIndex(int currentFrameIndex) {
        this.currentFrameIndex = currentFrameIndex;
    }

    public int getFrameDelay() {
        return frameDelay;
    }

    public void setFrameDelay(int frameDelay) {
        this.frameDelay = frameDelay;
    }

    public long getLastFrameTime() {
        return lastFrameTime;
    }

    public void setLastFrameTime(long lastFrameTime) {
        this.lastFrameTime = lastFrameTime;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }
}

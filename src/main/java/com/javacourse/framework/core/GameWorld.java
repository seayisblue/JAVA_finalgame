package com.javacourse.framework.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameWorld {
    private List<LifeCycle> entities;
    private List<LifeCycle> entitiesToAdd;
    private List<LifeCycle> entitiesToRemove;

    private boolean running;
    private int width;
    private int height;

    public GameWorld(int width, int height) {
        this.width = width;
        this.height = height;
        this.running = false;

        this.entities = new CopyOnWriteArrayList<>();
        this.entitiesToAdd = new ArrayList<>();
        this.entitiesToRemove = new ArrayList<>();
    }

    public void addEntity(LifeCycle entity) {
        synchronized (entitiesToAdd) {
            entitiesToAdd.add(entity);
        }
    }

    public void removeEntity(LifeCycle entity) {
        synchronized (entitiesToRemove) {
            entitiesToRemove.add(entity);
        }
    }

    public List<LifeCycle> getEntities() {
        return new ArrayList<>(entities);
    }

    @SuppressWarnings("unchecked")
    public <T extends LifeCycle> List<T> getEntitiesByType(Class<T> type) {
        List<T> result = new ArrayList<>();
        for (LifeCycle entity : entities) {
            if (type.isInstance(entity)) {
                result.add((T) entity);
            }
        }
        return result;
    }


    public void update() {
        if (!running) {
            return;
        }
        synchronized (entitiesToAdd) {
            if (!entitiesToAdd.isEmpty()) {
                for (LifeCycle entity : entitiesToAdd) {
                    if (entity == null) continue;
                    entities.add(entity);
                }
                entitiesToAdd.clear();
            }
        }

        for (LifeCycle entity : entities) {
            if (entity != null && entity.isAlive()) {
                entity.update();
            }
        }

        synchronized (entitiesToRemove) {
            if (!entitiesToRemove.isEmpty()) {
                entities.removeAll(entitiesToRemove);
                entitiesToRemove.clear();
            }
        }

        entities.removeIf(entity -> entity == null || !entity.isAlive());
    }

    public void clear() {
        entities.clear();
        entitiesToAdd.clear();
        entitiesToRemove.clear();

    }

    public void start() {
        this.running = true;
    }
    public void stop() {
        this.running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}

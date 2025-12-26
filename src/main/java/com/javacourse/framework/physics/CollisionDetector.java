package com.javacourse.framework.physics;

import com.javacourse.framework.core.GameEntity;

import java.util.ArrayList;
import java.util.List;

public class CollisionDetector {
    public static boolean checkCollision(GameEntity a, GameEntity b) {
        if (a == null || b == null) {
            return false;
        }
        if (!a.isAlive() || !b.isAlive()) {
            return false;
        }
        return a.getBounds().intersects(b.getBounds());
    }

    public static GameEntity checkCollision(GameEntity entity, List<? extends GameEntity> others) {
        if (entity == null || others == null) {
            return null;
        }
        for (GameEntity other : others) {
            if (entity!=other &&checkCollision(entity, other)) {
                return other;
            }
        }
        return null;
    }

    public static List<GameEntity> getAllCollisions(GameEntity entity, List<? extends GameEntity> others) {
        List<GameEntity> collisions = new ArrayList<>();
        if (entity == null || others == null) {
            return collisions;
        }
        for (GameEntity other : others) {
            if (entity!=other &&checkCollision(entity, other)) {
                collisions.add(other);
            }
        }
        return collisions;
    }

    public static boolean isPointInsideEntity(int px, int py, GameEntity entity) {
        if (entity == null) {
            return false;
        }
        return entity.getBounds().contains(px, py);
    }

    public static boolean checkBoundsCollision(Bounds a, Bounds b) {
        if (a == null || b == null) {
            return false;
        }
        return a.intersects(b);
    }

    private CollisionDetector() {
    }
}

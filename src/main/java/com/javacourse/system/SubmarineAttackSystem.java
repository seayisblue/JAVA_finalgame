package com.javacourse.system;

import com.javacourse.config.Constants;
import com.javacourse.entity.Ship;
import com.javacourse.entity.Submarine;
import com.javacourse.factory.EntityFactory;
import com.javacourse.framework.core.GameWorld;

import java.util.List;

public class SubmarineAttackSystem {
    private EntityFactory factory;
    private GameWorld world;
    private Ship targetShip;

    public SubmarineAttackSystem(EntityFactory factory, GameWorld world, Ship targetShip) {
        this.factory = factory;
        this.world = world;
        this.targetShip = targetShip;
    }

    public void update() {
        if (targetShip == null) {
            return;
        }
        List<Submarine> submarines = world.getEntitiesByType(Submarine.class);
        for (Submarine submarine : submarines) {
            if (!submarine.isAlive()) {
                continue;
            }
            if (submarine.getSubmarineType() != Constants.SUBMARINE_TYPE_ELITE) {
                continue;
            }
            if (!isSubmarineOnScreen(submarine)) {
                continue;
            }
            double startX = submarine.getX() + submarine.getWidth() / 2.0 - Constants.SUBMARINE_BULLET_WIDTH / 2.0;
            double startY = submarine.getY() + submarine.getHeight() / 2.0 - Constants.SUBMARINE_BULLET_HEIGHT / 2.0;
            double targetX = targetShip.getX() + targetShip.getWidth() / 2.0;
            double targetY = targetShip.getY() + targetShip.getHeight() / 2.0;
            world.addEntity(factory.createSubmarineBullet(startX, startY, targetX, targetY));
            submarine.markFired();
        }
    }

    private boolean isSubmarineOnScreen(Submarine submarine) {
        double x = submarine.getX();
        double y = submarine.getY();
        double width = submarine.getWidth();
        double height = submarine.getHeight();
        return x + width > 0
                && x < Constants.WINDOW_WIDTH
                && y + height > 0
                && y < Constants.WINDOW_HEIGHT;
    }

}

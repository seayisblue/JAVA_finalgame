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

    public SubmarineAttackSystem(EntityFactory factory, GameWorld world) {
        this.factory = factory;
        this.world = world;
    }

    public void update() {
        List<Ship> ships = world.getEntitiesByType(Ship.class);
        List<Submarine> submarines = world.getEntitiesByType(Submarine.class);
        for (Submarine submarine : submarines) {
            if (!submarine.isAlive()) {
                continue;
            }
            if (submarine.getSubmarineType() != Constants.SUBMARINE_TYPE_ELITE) {
                continue;
            }
            if (!submarine.canFire()) {
                continue;
            }
            Ship targetShip = findClosestShip(submarine, ships);
            if (targetShip == null) {
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

    private Ship findClosestShip(Submarine submarine, List<Ship> ships) {
        Ship closest = null;
        double closestDistance = Double.MAX_VALUE;
        for (Ship ship : ships) {
            if (!ship.isAlive()) {
                continue;
            }
            double dx = submarine.getX() - ship.getX();
            double dy = submarine.getY() - ship.getY();
            double distance = dx * dx + dy * dy;
            if (distance < closestDistance) {
                closestDistance = distance;
                closest = ship;
            }
        }
        return closest;
    }
}

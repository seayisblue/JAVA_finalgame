package com.javacourse.system;

import com.javacourse.entity.DepthCharge;
import com.javacourse.entity.Explosion;
import com.javacourse.entity.Ship;
import com.javacourse.entity.Submarine;
import com.javacourse.entity.SubmarineBullet;
import com.javacourse.factory.EntityFactory;
import com.javacourse.framework.core.GameWorld;
import com.javacourse.framework.physics.CollisionDetector;

import java.util.List;

public class CollisionHandler {

    private GameWorld world;
    private EntityFactory factory;
    private ScoreSystem scoreSystem;
    private LifeSystem lifeSystem;

    public CollisionHandler(GameWorld world, EntityFactory factory, ScoreSystem scoreSystem, LifeSystem lifeSystem) {
        this.world = world;
        this.factory = factory;
        this.scoreSystem = scoreSystem;
        this.lifeSystem = lifeSystem;
    }

    public void update() {
        checkBombSubmarineCollisions();
        checkSubmarineBulletShipCollisions();
    }

//  TODO 可以拓展为有血量和攻击力, 通过在GameEntity里面添加health和attack, 但是我太懒了就一击必杀算了
    private void checkBombSubmarineCollisions() {
        List<DepthCharge> bombs = world.getEntitiesByType(DepthCharge.class);
        List<Submarine> submarines = world.getEntitiesByType(Submarine.class);

        for (DepthCharge bomb : bombs) {
            if (!bomb.isAlive()) continue;

            for (Submarine submarine : submarines) {
                if (!submarine.isAlive()) continue;

                if (CollisionDetector.checkCollision(bomb, submarine)) {

                    handleBombCollision(bomb, submarine);
                }
            }
        }
    }

    private void handleBombCollision(DepthCharge bomb, Submarine submarine) {
        int playerId = bomb.getOwnerId();

        scoreSystem.addScore(playerId, submarine.getScore());
        double explosionX = submarine.getX();
        double explosionY = submarine.getY();
        Explosion explosion = factory.createExplosion(explosionX, explosionY);
        world.addEntity(explosion);

        bomb.destroy();
        submarine.destroy();
    }

    private void checkSubmarineBulletShipCollisions() {
        List<SubmarineBullet> bullets = world.getEntitiesByType(SubmarineBullet.class);
        List<Ship> ships = world.getEntitiesByType(Ship.class);

        for (SubmarineBullet bullet : bullets) {
            if (!bullet.isAlive()) {
                continue;
            }

            for (Ship ship : ships) {
                if (!ship.isAlive()) {
                    continue;
                }

                if (CollisionDetector.checkCollision(bullet, ship)) {
                    handleSubmarineBulletCollision(bullet, ship);
                }
            }
        }
    }

    private void handleSubmarineBulletCollision(SubmarineBullet bullet, Ship ship) {
        double explosionX = ship.getX();
        double explosionY = ship.getY();
        Explosion explosion = factory.createExplosion(explosionX, explosionY);
        world.addEntity(explosion);

        if (lifeSystem != null) {
            lifeSystem.damagePlayer(ship.getPlayerId(), 1);
        }

        bullet.destroy();
    }
}

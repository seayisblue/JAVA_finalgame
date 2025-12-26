package com.javacourse.system;

import com.javacourse.entity.Submarine;
import com.javacourse.factory.EntityFactory;
import com.javacourse.framework.core.GameWorld;
import com.javacourse.config.Constants;
import com.javacourse.framework.util.Direction;
import com.javacourse.framework.util.MathUtil;

//TODO: 生成的interval不该是固定的, 而是[1,5]秒之间随机, 要改.
public class SubmarineSpawner {
    private EntityFactory factory;
    private GameWorld world;

    private long lastSpawnTime;
    private int maxSubmarines;
    private int nextSpawnInterval;
    private int minInterval;
    private int maxInterval;

    public SubmarineSpawner(EntityFactory factory, GameWorld world) {
        this.factory = factory;
        this.world = world;
        this.maxSubmarines = Constants.SUBMARINE_MAX_COUNT;
        this.lastSpawnTime = System.currentTimeMillis();

        this.minInterval = Constants.SUBMARINE_SPAWN_MIN_INTERVAL;
        this.maxInterval = Constants.SUBMARINE_SPAWN_MAX_INTERVAL;
        this.nextSpawnInterval = minInterval;
    }

    public void update() {
        int currentCount = world.getEntitiesByType(Submarine.class).size();
        if ( currentCount >= maxSubmarines) {
            return;
        }
        Long currentTime = System.currentTimeMillis();
        if (currentTime - lastSpawnTime >= nextSpawnInterval) {
            spawnSubmarine();
            lastSpawnTime = currentTime;
            nextSpawnInterval = randomInterval();
        }
    }

    public void spawnSubmarine() {
        boolean spawnFromLeft = MathUtil.getRandomDouble(0, 1) > 0.5;

        Direction direction;
        double x;

        if (spawnFromLeft) {
            x = -Constants.SUBMARINE_WIDTH;
            direction = Direction.RIGHT;
        } else {
            x = Constants.WINDOW_WIDTH;
            direction = Direction.LEFT;
        }

        double y = MathUtil.getRandomInt(
                Constants.SUBMARINE_ZONE_TOP,
                Constants.SUBMARINE_ZONE_BOTTOM
        );

        Submarine submarine = factory.createRandomSubmarine(x, y, direction);
        world.addEntity(submarine);
    }

    public void reset() {
        lastSpawnTime = System.currentTimeMillis();
        nextSpawnInterval = randomInterval();
    }

    private int randomInterval() {
        return (int)(minInterval + Math.random() * (maxInterval - minInterval));
    }
}

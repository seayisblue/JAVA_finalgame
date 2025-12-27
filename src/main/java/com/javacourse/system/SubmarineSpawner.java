package com.javacourse.system;

import com.javacourse.entity.Submarine;
import com.javacourse.game.Difficulty;
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
    private int friendlyLimit;

    public SubmarineSpawner(EntityFactory factory, GameWorld world) {
        this.factory = factory;
        this.world = world;
        this.maxSubmarines = Constants.SUBMARINE_MAX_COUNT;
        this.lastSpawnTime = System.currentTimeMillis();

        this.minInterval = Constants.SUBMARINE_SPAWN_MIN_INTERVAL;
        this.maxInterval = Constants.SUBMARINE_SPAWN_MAX_INTERVAL;
        this.nextSpawnInterval = minInterval;
        this.friendlyLimit = Constants.FRIENDLY_SUBMARINE_NORMAL_MAX;
    }

    public void applyDifficulty(Difficulty difficulty) {
        if (difficulty == null) {
            return;
        }
        switch (difficulty) {
            case EASY:
                maxSubmarines = Constants.EASY_MAX_SUBMARINES;
                minInterval = Constants.EASY_SPAWN_MIN_INTERVAL;
                maxInterval = Constants.EASY_SPAWN_MAX_INTERVAL;
                friendlyLimit = Constants.FRIENDLY_SUBMARINE_EASY_MAX;
                Submarine.setEnemySpeedRange(Constants.ENEMY_SPEED_EASY_MIN, Constants.ENEMY_SPEED_EASY_MAX);
                Submarine.setEliteFireCooldown(Constants.ELITE_FIRE_COOLDOWN_EASY);
                break;
            case HARD:
                maxSubmarines = Constants.HARD_MAX_SUBMARINES;
                minInterval = Constants.HARD_SPAWN_MIN_INTERVAL;
                maxInterval = Constants.HARD_SPAWN_MAX_INTERVAL;
                friendlyLimit = Constants.FRIENDLY_SUBMARINE_HARD_MAX;
                Submarine.setEnemySpeedRange(Constants.ENEMY_SPEED_HARD_MIN, Constants.ENEMY_SPEED_HARD_MAX);
                Submarine.setEliteFireCooldown(Constants.ELITE_FIRE_COOLDOWN_HARD);
                break;
            case NORMAL:
            default:
                maxSubmarines = Constants.SUBMARINE_MAX_COUNT;
                minInterval = Constants.SUBMARINE_SPAWN_MIN_INTERVAL;
                maxInterval = Constants.SUBMARINE_SPAWN_MAX_INTERVAL;
                friendlyLimit = Constants.FRIENDLY_SUBMARINE_NORMAL_MAX;
                Submarine.setEnemySpeedRange(Constants.ENEMY_SPEED_NORMAL_MIN, Constants.ENEMY_SPEED_NORMAL_MAX);
                Submarine.setEliteFireCooldown(Constants.ELITE_FIRE_COOLDOWN_NORMAL);
                break;
        }
        nextSpawnInterval = randomInterval();
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

        int friendlyCount = countFriendlySubmarines();
        int type;
        if (friendlyCount >= friendlyLimit) {
            type = MathUtil.getRandomInt(Constants.SUBMARINE_TYPE_ENEMY, Constants.SUBMARINE_TYPE_ELITE);
        } else {
            type = MathUtil.getRandomInt(Constants.SUBMARINE_TYPE_FRIENDLY, Constants.SUBMARINE_TYPE_ELITE);
        }
        Submarine submarine = factory.createCertainSubmarine(x, y, direction, type);
        world.addEntity(submarine);
    }

    public void reset() {
        lastSpawnTime = System.currentTimeMillis();
        nextSpawnInterval = randomInterval();
    }

    private int randomInterval() {
        return (int)(minInterval + Math.random() * (maxInterval - minInterval));
    }

    private int countFriendlySubmarines() {
        int count = 0;
        for (Submarine submarine : world.getEntitiesByType(Submarine.class)) {
            if (submarine.getSubmarineType() == Constants.SUBMARINE_TYPE_FRIENDLY) {
                count++;
            }
        }
        return count;
    }
}

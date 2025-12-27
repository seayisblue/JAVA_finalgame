package com.javacourse.factory;

import com.javacourse.entity.DepthCharge;
import com.javacourse.entity.Explosion;
import com.javacourse.entity.Ship;
import com.javacourse.entity.Submarine;
import com.javacourse.entity.SubmarineBullet;
import com.javacourse.config.Constants;
import com.javacourse.framework.loader.ResourceManager;
import com.javacourse.framework.util.Direction;
import com.javacourse.framework.util.MathUtil;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class EntityFactory {
    private ResourceManager resourceManager;

    private List<BufferedImage> diffShipImages;
    private List<BufferedImage> friendlySubmarineFrames;
    private List<BufferedImage> enemySubmarineFrames;
    private List<BufferedImage> eliteSubmarineFrames;
    private BufferedImage bombImage;
    private BufferedImage enemyBombImage;
    private List<BufferedImage> explosionFrames;

    public EntityFactory() {
        this.resourceManager = ResourceManager.getInstance();
        loadResources();
    }

    private void loadResources() {

        diffShipImages = new ArrayList<>();
        BufferedImage ship0 = resourceManager.getImage("ship0");
        BufferedImage ship1 = resourceManager.getImage("ship1");
        if (ship0 != null) diffShipImages.add(ship0);
        if (ship1 != null) diffShipImages.add(ship1);

        friendlySubmarineFrames = new ArrayList<>();
        BufferedImage h1 = resourceManager.getImage("h1");
        BufferedImage h2 = resourceManager.getImage("h2");
        if (h1 != null) friendlySubmarineFrames.add(h1);
        if (h2 != null) friendlySubmarineFrames.add(h2);

        enemySubmarineFrames = new ArrayList<>();
        BufferedImage q1 = resourceManager.getImage("q1");
        BufferedImage q2 = resourceManager.getImage("q2");
        if (q1 != null) enemySubmarineFrames.add(q1);
        if (q2 != null) enemySubmarineFrames.add(q2);

        eliteSubmarineFrames = new ArrayList<>();
        BufferedImage r1 = resourceManager.getImage("r1");
        BufferedImage r2 = resourceManager.getImage("r2");
        if (r1 != null) eliteSubmarineFrames.add(r1);
        if (r2 != null) eliteSubmarineFrames.add(r2);

        bombImage = resourceManager.getImage("boom");
        enemyBombImage = resourceManager.getImage("boom2");

        explosionFrames = new ArrayList<>();
        BufferedImage b0 = resourceManager.getImage("b");
        BufferedImage b1 = resourceManager.getImage("b1");
        BufferedImage b2 = resourceManager.getImage("b2");
        if (b0 != null) explosionFrames.add(b0);
        if (b1 != null) explosionFrames.add(b1);
        if (b2 != null) explosionFrames.add(b2);
    }

    public Ship createShip(int playerId, double x, double y) {
        if (!diffShipImages.isEmpty()) {
            return new Ship(playerId, x, y, diffShipImages);
        }
        return new Ship(playerId, x, y, (BufferedImage) null);
    }

    public DepthCharge createBomb(double x, double y, int ownerId) {
        return new DepthCharge(x, y, ownerId, bombImage);
    }

    public Submarine createRandomSubmarine (double x, double y, Direction direction) {
        int type = MathUtil.getRandomInt(0, 2);
        return createCertainSubmarine(x, y, direction, type);
    }

    //TODO: 生成的逻辑必须优化. 从左边来的submarine应该方向为right, 反之情况相反. 而不是随机方向!
    public Submarine createCertainSubmarine (double x, double y, Direction direction, int type) {
        List<BufferedImage> frames = getSubmarineFrames(type);
        if (frames != null && !frames.isEmpty()) {
            return new Submarine(x, y, direction, type, frames);
        }
        return new Submarine(x, y, direction, type, (BufferedImage) null);
    }

    public Explosion createExplosion(double x, double y) {
        return new Explosion(x, y, explosionFrames);
    }

    public SubmarineBullet createSubmarineBullet(double x, double y, double targetX, double targetY) {
        return new SubmarineBullet(x, y, targetX, targetY, enemyBombImage);
    }

    private List<BufferedImage> getSubmarineFrames(int type) {
        if (type == Constants.SUBMARINE_TYPE_FRIENDLY) {
            return friendlySubmarineFrames;
        }
        if (type == Constants.SUBMARINE_TYPE_ELITE) {
            return eliteSubmarineFrames;
        }
        return enemySubmarineFrames;
    }

}

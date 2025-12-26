package com.javacourse.factory;

import com.javacourse.entity.DepthCharge;
import com.javacourse.entity.Explosion;
import com.javacourse.entity.Ship;
import com.javacourse.entity.Submarine;
import com.javacourse.framework.loader.ResourceManager;
import com.javacourse.framework.util.Direction;
import com.javacourse.framework.util.MathUtil;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class EntityFactory {
    private ResourceManager resourceManager;

    private List<BufferedImage> diffShipImages;
    private List<BufferedImage> diffSubmarineImages;
    private BufferedImage bombImage;
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

        diffSubmarineImages = new ArrayList<>();
        diffSubmarineImages = new ArrayList<>();
        diffSubmarineImages.add(resourceManager.getImage("q1"));
        diffSubmarineImages.add(resourceManager.getImage("q2"));
        diffSubmarineImages.add(resourceManager.getImage("r1"));
        diffSubmarineImages.add(resourceManager.getImage("h2"));

        bombImage = resourceManager.getImage("boom");

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
        System.out.println("Creating depth charge at (" + x + ", " + y + ") by player " + ownerId);
        return new DepthCharge(x, y, ownerId, bombImage);
    }

    public Submarine createRandomSubmarine (double x, double y, Direction direction) {
        System.out.println("Creating submarine at (" + x + ", " + y + ") going " + direction);
        int type = MathUtil.getRandomInt(0, 3);
        return createCertainSubmarine(x, y, direction, type);
    }

    //TODO: 生成的逻辑必须优化. 从左边来的submarine应该方向为right, 反之情况相反. 而不是随机方向!
    public Submarine createCertainSubmarine (double x, double y, Direction direction, int type) {
        int index = type % diffSubmarineImages.size();
        if (index >= 0 && index < diffSubmarineImages.size()) {
            BufferedImage subImage = diffSubmarineImages.get(index);
            return new Submarine(x, y, direction, type, subImage);
        }
        return new Submarine( x, y, direction, type, (BufferedImage)null);
    }

    public Explosion createExplosion(double x, double y) {
        System.out.println("Creating explosion at (" + x + ", " + y + ")");
        return new Explosion(x, y, explosionFrames);
    }

}

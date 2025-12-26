package com.javacourse.framework.loader;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class ResourceManager {
    private static ResourceManager instance;

    private Map<String, BufferedImage> images;

    public ResourceManager() {
        images = new HashMap<>();
    }

    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    public void loadImage(String key, String imagePath) {
        BufferedImage image = ImageLoader.loadImage(imagePath);
        if (image != null) {
            images.put(key, image);
        }
    }

    public BufferedImage getImage(String key) {
        return images.get(key);
    }

    public void loadImages(Map<String, String> imagePaths) {
        for (Map.Entry<String, String> entry : imagePaths.entrySet()) {
            loadImage(entry.getKey(), entry.getValue());
        }
    }

    public void clear() {
        images.clear();
        ImageLoader.clearCache();
    }

    public boolean hasImage(String key) {
        return images.containsKey(key);
    }

}

package com.javacourse.framework.loader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageLoader {
    private static final Map<String, BufferedImage> imageCache = new HashMap<>();

    public static BufferedImage loadImage(String path) {
        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        }

        try {
            InputStream stream = ImageLoader.class.getResourceAsStream(path);
            if (stream == null) {
                System.err.println("Image not found: " + path);
                return null;
            }
            BufferedImage image = ImageIO.read(stream);
            stream.close();
            imageCache.put(path, image);

            return image;

        } catch (IOException e) {
            System.err.println("Failed to load image: " + path);
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedImage loadImage(String path, int width, int height) {
        BufferedImage original = loadImage(path);
        if (original == null) {
            return null;
        }
        Image scaled = original.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        resized.getGraphics().drawImage(scaled, 0, 0, null);

        return resized;
    }

    public static void clearCache() {
        imageCache.clear();
    }

    public static int getCacheSize() {
        return imageCache.size();
    }

    private ImageLoader() {
    }





}

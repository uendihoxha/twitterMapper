package util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton that caches images loaded from twitter urls.
 */
public class ImageCache {
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private Map<String, BufferedImage> imageCache = new HashMap<>();
    private Map<String, String> pathCache = new HashMap<>();

    private ImageCache() {
    }

    public static ImageCache getInstance() {
        return LazySingleton.INSTANCE;
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private BufferedImage getImage(String url) {
        BufferedImage bufferedImage = imageCache.get(url);

        if (bufferedImage == null) {
            bufferedImage = Util.getImageFromURL(url);
            imageCache.put(url, bufferedImage);
        }

        return bufferedImage;
    }

    private String saveImage(BufferedImage image, String path) {
        File directory = new File("data/imagecache");
        if (!directory.isDirectory()) {
            directory.mkdir();
        }

        String pathString = "data/imagecache/" + path + ".png";
        File newFile = new File(pathString);
        pathString = newFile.getAbsolutePath();

        if (newFile.canRead()) {
            return pathString;
        }

        try {
            ImageIO.write(image, "png", newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pathString;
    }

    public void loadImage(String url) {
        BufferedImage imageFromCache = imageCache.get(url);
        if (imageFromCache == null) {
            imageCache.put(url, Util.DEFAULT_IMAGE);
            Thread thread = new Thread(() -> {
                BufferedImage imageFromURL = Util.getImageFromURL(url);
                SwingUtilities.invokeLater(() -> imageCache.put(url, imageFromURL));
            });
            thread.run();
        }
    }

    public String getImagePath(String url) {
        return saveImage(getImage(url), getHashURL(url));
    }

    private String getHashURL(String url) {
        String hash = pathCache.get(url);
        if (hash == null) {
            hash = getSha256Hash(url);
        }
        return hash;
    }

    private String getSha256Hash(String data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = data.getBytes();
            messageDigest.update(bytes);
            byte[] hash = messageDigest.digest();
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Can't find SHA-256");
        }
    }

    private static class LazySingleton {
        private static final ImageCache INSTANCE = new ImageCache();
    }
}
package com.example.restuarantms.util;

import javafx.scene.image.Image;
import java.io.InputStream;

/**
 * ImageUtil - utility class for loading images from classpath resources ONLY
 * All images must be stored as resource paths (e.g., "/Images/filename.png")
 */
public class ImageUtil {

    /**
     * Load image from classpath resource ONLY
     * Paths must be in format: /Images/filename.png
     *
     * @param path - resource path to image (e.g., "/Images/Burrito.png")
     * @param width - desired width
     * @param height - desired height
     * @param preserveRatio - whether to preserve aspect ratio
     * @return Image object or null if loading fails
     */
    public static Image loadImageFromResource(String path, double width, double height, boolean preserveRatio) {
        if (path == null || path.isEmpty()) {
            return null;
        }

        try {
            // Convert any path to resource path format (extract filename)
            String resourcePath = convertToResourcePath(path);

            // Ensure path starts with /
            if (!resourcePath.startsWith("/")) {
                resourcePath = "/" + resourcePath;
            }

            // Load from classpath resources ONLY
            InputStream imageStream = ImageUtil.class.getResourceAsStream(resourcePath);
            if (imageStream != null) {
                Image image = new Image(imageStream, width, height, preserveRatio, true);
                imageStream.close();
                if (!image.isError()) {
                    return image;
                }
            }
        } catch (Exception e) {
            // Image loading failed
            System.err.println("Failed to load image from resource path: " + path);
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Load image from resource path (does not preserve aspect ratio)
     *
     * @param path - resource path to image
     * @param width - desired width
     * @param height - desired height
     * @return Image object or null if loading fails
     */
    public static Image loadImageFromResource(String path, double width, double height) {
        return loadImageFromResource(path, width, height, false);
    }

    /**
     * Convert any path to resource path format
     * Extracts filename from absolute paths and returns /Images/filename
     *
     * @param path - any path (absolute file path or resource path)
     * @return resource path (e.g., "/Images/filename.png")
     */
    public static String convertToResourcePath(String path) {
        if (path == null || path.isEmpty()) {
            return path;
        }

        try {
            // If already a resource path, return as is
            if (path.startsWith("/Images/")) {
                return path;
            }

            // Remove file: prefix if present
            String cleanPath = path.replaceFirst("^file:", "");

            // Normalize path separators (handle Windows backslashes)
            String normalizedPath = cleanPath.replace("\\", "/");

            // Extract filename from path
            String filename = normalizedPath;
            int lastSlash = normalizedPath.lastIndexOf("/");
            if (lastSlash != -1) {
                filename = normalizedPath.substring(lastSlash + 1);
            }

            // Return resource path with filename
            return "/Images/" + filename;
        } catch (Exception e) {
            // If conversion fails, try to extract just filename
            try {
                String filename = path;
                if (path.contains("/")) {
                    filename = path.substring(path.lastIndexOf("/") + 1);
                }
                if (filename.contains("\\")) {
                    filename = filename.substring(filename.lastIndexOf("\\") + 1);
                }
                return "/Images/" + filename;
            } catch (Exception e2) {
                return "/Images/" + path;
            }
        }
    }
}
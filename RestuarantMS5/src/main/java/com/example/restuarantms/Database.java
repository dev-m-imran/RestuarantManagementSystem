package com.example.restuarantms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.io.File;

/**
 * Database class - handles SQLite database connection
 */
public class Database {

    /**
     * Connect to SQLite database
     * Creates database file if it doesn't exist
     * @return Connection object
     */
    public static Connection connectDataBase() {
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Create database file in project directory
            String dbPath = "gdkms.db";
            String url = "jdbc:sqlite:" + dbPath;

            Connection conn = DriverManager.getConnection(url);

            // Enable WAL mode for better concurrency (allows multiple readers)
            Statement stmt = conn.createStatement();
            stmt.execute("PRAGMA journal_mode=WAL");
            stmt.close();

            // Create tables if they don't exist
            createTables(conn);

            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Close database resources properly
     */
    public static void closeResources(Connection conn, Statement stmt, java.sql.ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create all necessary tables if they don't exist
     */
    private static void createTables(Connection conn) {
        try {
            Statement stmt = conn.createStatement();

            // Create employees table
            stmt.execute("CREATE TABLE IF NOT EXISTS employees (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username VARCHAR(110) NOT NULL, " +
                    "password VARCHAR(110) NOT NULL, " +
                    "question VARCHAR(110) NOT NULL, " +
                    "answer VARCHAR(110) NOT NULL, " +
                    "date DATE DEFAULT (date('now'))" +
                    ")");

            // Create products table
            stmt.execute("CREATE TABLE IF NOT EXISTS products (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "product_id VARCHAR(100) NOT NULL, " +
                    "product_name VARCHAR(120) NOT NULL, " +
                    "type VARCHAR(130) NOT NULL, " +
                    "stock INTEGER NOT NULL, " +
                    "price REAL NOT NULL, " +
                    "status VARCHAR(100) DEFAULT 'Available', " +
                    "image VARCHAR(500) NOT NULL, " +
                    "date DATE DEFAULT (date('now'))" +
                    ")");

            // Create customers table
            stmt.execute("CREATE TABLE IF NOT EXISTS customers (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "customer_id INTEGER NOT NULL, " +
                    "product_id VARCHAR(100) NOT NULL, " +
                    "product_name VARCHAR(120) NOT NULL, " +
                    "quantity INTEGER NOT NULL, " +
                    "price REAL NOT NULL, " +
                    "date DATE DEFAULT (date('now')), " +
                    "em_username TEXT NOT NULL, " +
                    "paid INTEGER DEFAULT 0" +
                    ")");

            // Create receipts table
            stmt.execute("CREATE TABLE IF NOT EXISTS receipts (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "customer_id INTEGER NOT NULL, " +
                    "total REAL NOT NULL, " +
                    "amount REAL NOT NULL, " +
                    "cu_change REAL NOT NULL, " +
                    "date DATETIME DEFAULT (datetime('now')), " +
                    "em_username TEXT NOT NULL" +
                    ")");

            stmt.close();

            // Insert sample data if database is empty
            insertSampleData(conn);

            // Update all existing image paths to resource paths
            updateImagePathsToResources(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Map product name to appropriate image filename
     * Matches product names to available images in the Images folder
     * This is public so it can be used when adding/updating products
     */
    public static String mapProductNameToImage(String productName) {
        if (productName == null || productName.isEmpty()) {
            return "/Images/Burrito.png"; // default
        }

        String name = productName.toLowerCase().trim();

        // Map product names to images
        if (name.contains("burrito")) {
            return "/Images/Burrito.png";
        } else if (name.contains("quesadilla")) {
            return "/Images/quesadilla.png";
        } else if (name.contains("doner") || name.contains("wrap")) {
            return "/Images/DonerWrap.png";
        } else if (name.contains("kebab") || name.contains("og")) {
            return "/Images/OG-kebab.jpg";
        } else if (name.contains("coca") || name.contains("cola")) {
            return "/Images/coca.png";
        } else if (name.contains("oasis")) {
            return "/Images/oasis.png";
        } else if (name.contains("fanta")) {
            return "/Images/fanta.png";
        } else if (name.contains("dr pepper") || name.contains("drpepper") || name.contains("pepper")) {
            return "/Images/drPepper.png";
        } else if (name.contains("sprite")) {
            return "/Images/sprite.png";
        } else if (name.contains("burger") || name.contains("deluxe")) {
            return "/Images/Burrito.png"; // Use Burrito as default for burgers
        } else if (name.contains("pizza") || name.contains("chicken pizza")) {
            return "/Images/quesadilla.png"; // Use quesadilla for pizza
        } else if (name.contains("grilled") || name.contains("chicken")) {
            return "/Images/DonerWrap.png"; // Use DonerWrap for grilled chicken
        } else if (name.contains("pasta") || name.contains("carbonara")) {
            return "/Images/Burrito.png"; // Use Burrito for pasta
        } else if (name.contains("orange") || name.contains("juice")) {
            return "/Images/oasis.png"; // Use Oasis for orange juice
        } else if (name.contains("coffee")) {
            return "/Images/fanta.png"; // Use Fanta for coffee
        } else if (name.contains("iced") || name.contains("tea")) {
            return "/Images/drPepper.png"; // Use Dr Pepper for iced tea
        }

        // Default fallback
        return "/Images/Burrito.png";
    }

    /**
     * Update all image paths in products table to use resource paths
     * Maps product names to correct images and converts paths to resource format
     */
    private static void updateImagePathsToResources(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery("SELECT id, product_name, image FROM products");

            while (rs.next()) {
                int id = rs.getInt("id");
                String productName = rs.getString("product_name");
                String imagePath = rs.getString("image");

                // Map product name to correct image
                String correctImagePath = mapProductNameToImage(productName);

                // If current image doesn't match, update it
                if (!correctImagePath.equals(imagePath)) {
                    PreparedStatement updateStmt = conn.prepareStatement("UPDATE products SET image = ? WHERE id = ?");
                    updateStmt.setString(1, correctImagePath);
                    updateStmt.setInt(2, id);
                    updateStmt.executeUpdate();
                    updateStmt.close();
                } else if (imagePath != null && !imagePath.startsWith("/Images/")) {
                    // Convert to resource path if it's not already one
                    String resourcePath = com.example.restuarantms.util.ImageUtil.convertToResourcePath(imagePath);
                    PreparedStatement updateStmt = conn.prepareStatement("UPDATE products SET image = ? WHERE id = ?");
                    updateStmt.setString(1, resourcePath);
                    updateStmt.setInt(2, id);
                    updateStmt.executeUpdate();
                    updateStmt.close();
                }
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Insert sample data for demonstration
     * Only inserts if tables are empty
     */
    private static void insertSampleData(Connection conn) {
        try {
            Statement stmt = conn.createStatement();

            // Check if admin account exists
            java.sql.ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM employees WHERE username = 'admin'");
            boolean adminExists = rs.getInt("count") > 0;
            rs.close();

            // Insert admin account if it doesn't exist
            if (!adminExists) {
                stmt.execute("INSERT INTO employees (username, password, question, answer) VALUES " +
                        "('admin', 'password123', 'What is your favourite Color?', 'blue')");
            }

            // Check if imran account exists
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM employees WHERE username = 'imran'");
            boolean imranExists = rs.getInt("count") > 0;
            rs.close();

            // Insert imran account if it doesn't exist
            if (!imranExists) {
                stmt.execute("INSERT INTO employees (username, password, question, answer) VALUES " +
                        "('imran', '12345678', 'What is your favourite Color?', 'red')");
            }

            // Insert sample food items (Meals) - check each individually
            // Map product names to appropriate images
            String[] productIds = {"FD-001", "FD-002", "FD-003", "FD-004", "DR-001", "DR-002", "DR-003", "DR-004"};
            String[] productNames = {"Burrito", "Quesadilla", "Doner Wrap", "OG Kebab",
                    "Coca Cola", "Oasis", "Fanta", "Dr Pepper"};
            String[] productTypes = {"Meals", "Meals", "Meals", "Meals", "Drinks", "Drinks", "Drinks", "Drinks"};
            int[] stocks = {50, 30, 40, 35, 100, 80, 60, 70};
            double[] prices = {12.99, 15.99, 18.99, 14.99, 2.99, 3.99, 4.99, 3.49};
            // Map images to match product names exactly
            String[] images = {"/Images/Burrito.png", "/Images/quesadilla.png",
                    "/Images/DonerWrap.png", "/Images/OG-kebab.jpg",
                    "/Images/coca.png", "/Images/oasis.png",
                    "/Images/fanta.png", "/Images/drPepper.png"};

            for (int i = 0; i < productIds.length; i++) {
                rs = stmt.executeQuery("SELECT COUNT(*) as count FROM products WHERE product_id = '" + productIds[i] + "'");
                boolean productExists = rs.getInt("count") > 0;
                rs.close();

                if (!productExists) {
                    stmt.execute("INSERT INTO products (product_id, product_name, type, stock, price, status, image) VALUES " +
                            "('" + productIds[i] + "', '" + productNames[i] + "', '" + productTypes[i] + "', " +
                            stocks[i] + ", " + prices[i] + ", 'Available', '" + images[i] + "')");
                }
            }

            // Check if receipts table is empty and add sample sales data
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM receipts");
            boolean receiptsEmpty = rs.getInt("count") == 0;
            rs.close();

            if (receiptsEmpty) {
                // Add sample receipts for dashboard display
                // Sample receipt 1
                stmt.execute("INSERT INTO receipts (customer_id, total, amount, cu_change, em_username, date) VALUES " +
                        "(1, 45.97, 50.00, 4.03, 'admin', datetime('now', '-2 days'))");

                // Sample receipt 2
                stmt.execute("INSERT INTO receipts (customer_id, total, amount, cu_change, em_username, date) VALUES " +
                        "(2, 28.98, 30.00, 1.02, 'admin', datetime('now', '-1 days'))");

                // Sample receipt 3 (today)
                stmt.execute("INSERT INTO receipts (customer_id, total, amount, cu_change, em_username, date) VALUES " +
                        "(3, 35.97, 40.00, 4.03, 'imran', datetime('now'))");

                // Sample receipt 4 (today)
                stmt.execute("INSERT INTO receipts (customer_id, total, amount, cu_change, em_username, date) VALUES " +
                        "(4, 18.99, 20.00, 1.01, 'imran', datetime('now'))");
            }

            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
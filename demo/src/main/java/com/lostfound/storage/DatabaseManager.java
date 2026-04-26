package com.lostfound.storage;

// import java.io.File;
// import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.lostfound.controllers.DashboardController;
import com.lostfound.models.FoundItem;
import com.lostfound.models.Item;
import com.lostfound.models.LostItem;
import com.lostfound.models.Notification;
import com.lostfound.models.Student;

public class DatabaseManager {
    private static Properties props = new Properties();

    static {
        try (InputStream input = DatabaseManager.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("Sorry, unable to find config.properties in resources!");
            } else {
                props.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static final String HOST = props.getProperty("db.host");
    private static final String DB_NAME = props.getProperty("db.name");
    private static final String USER = props.getProperty("db.user");
    private static final String PASS = props.getProperty("db.pass");

    private static final String URL = "jdbc:mysql://" + HOST + ":3306/" + DB_NAME;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // 2. Register Student (Sign Up)
    public static boolean registerStudent(String name, String email, String password) {
        String query = "INSERT INTO students (name, email, password) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Authenticate Student (Login)
    public static Student loginStudent(String email, String password) {
        String query = "SELECT * FROM students WHERE email = ? AND password = ?";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Student(rs.getString("name"), rs.getString("email"), rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 1. Item Save Karna
    public static boolean saveItem(Item item) {
        String query = "INSERT INTO items (id, name, category, description, location, type, reported_by_email, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, item.getId());
            pstmt.setString(2, item.getName());
            pstmt.setString(3, item.getCategory());
            pstmt.setString(4, item.getDescription());
            pstmt.setString(5, item.getLocation());
            pstmt.setString(6, item.getType());
            pstmt.setString(7, item.getReportedByEmail());
            pstmt.setDate(8, java.sql.Date.valueOf(item.getDate()));

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. Sare Items Load Karna (Dashboard ke liye)
    public static List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        String query = "SELECT * FROM items";
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Item item;
                String type = rs.getString("type");
                if (type.equalsIgnoreCase("lost")) {
                    item = new LostItem(rs.getString("name"), rs.getString("category"),
                            rs.getString("description"), rs.getString("location"),
                            rs.getString("reported_by_email"));
                } else {
                    item = new FoundItem(rs.getString("name"), rs.getString("category"),
                            rs.getString("description"), rs.getString("location"),
                            rs.getString("reported_by_email"));
                }
                item.setId(rs.getString("id"));
                item.setDate(rs.getDate("date").toLocalDate());
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public static boolean updateItem(String id, Item updatedItem) {
        // 1. SQL Query String
        String query = "UPDATE items SET name = ?, category = ?, description = ?, location = ?, type = ?, date = ? WHERE id = ?";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            // 2. Values set karna (Index order ka khayal rakhein)
            pstmt.setString(1, updatedItem.getName());
            pstmt.setString(2, updatedItem.getCategory());
            pstmt.setString(3, updatedItem.getDescription());
            pstmt.setString(4, updatedItem.getLocation());
            pstmt.setString(5, updatedItem.getType());
            pstmt.setDate(6, java.sql.Date.valueOf(updatedItem.getDate()));

            // 3. WHERE clause wali ID
            pstmt.setString(7, id);

            // 4. Run karna (Badlao ke liye executeUpdate use hota hai)
            int rowsAffected = pstmt.executeUpdate();

            return rowsAffected > 0; // Agar 1 row update hui to true milega

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteItem(String id) {
        String deleteItemQuery = "DELETE FROM items WHERE id = ?";
        String deleteNotifQuery = "DELETE FROM notifications WHERE item_id = ?";

        try (Connection con = getConnection()) {
            try (PreparedStatement pstmtN = con.prepareStatement(deleteNotifQuery)) {
                pstmtN.setString(1, id);
                pstmtN.executeUpdate();
            }

            try (PreparedStatement pstmtI = con.prepareStatement(deleteItemQuery)) {
                pstmtI.setString(1, id);
                int rowsAffected = pstmtI.executeUpdate();

                DashboardController.refreshBadge();

                return rowsAffected > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean saveNotification(Notification notification) {
        String query = "INSERT INTO notifications (item_id, item_name, location, category, contact_email, receiver_email, date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, notification.getItemId());
            pstmt.setString(2, notification.getName());
            pstmt.setString(3, notification.getLocation());
            pstmt.setString(4, notification.getCategory());
            pstmt.setString(5, notification.getReportedByEmail());
            pstmt.setString(6, notification.getReceiverEmail());
            pstmt.setDate(7, java.sql.Date.valueOf(notification.getDate()));
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Notification> getAllNotifications() {
        String query = "SELECT * FROM notifications";
        List<Notification> notifications = new ArrayList<>();
        try (Connection conn = getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                Notification n = new Notification(
                        rs.getString("item_id"),
                        rs.getString("item_name"),
                        rs.getString("location"),
                        rs.getString("category"),
                        rs.getString("contact_email"),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("receiver_email"));
                notifications.add(n);
            }
            return notifications;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static boolean clearUserNotifications(String email) {
        String query = "DELETE FROM notifications WHERE receiver_email = ?";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            int rows = pstmt.executeUpdate();
            return rows >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}

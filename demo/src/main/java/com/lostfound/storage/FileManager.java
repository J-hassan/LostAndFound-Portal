package com.lostfound.storage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.lostfound.controllers.DashboardController;
import com.lostfound.models.Item;
import com.lostfound.models.Notification;
import com.lostfound.models.Student;

public class FileManager {

    private static final String DATA_DIR = System.getProperty("user.home") + File.separator + "LostAndFoundData"
            + File.separator;
    private static final String STUDENT_DATA_FILE = DATA_DIR + "students.dat";
    private static final String ITEMS_FILE = DATA_DIR + "items.dat";
    private static final String NOTIFICATIONS_FILE = DATA_DIR + "notifications.dat";

    // --- Helper method to ensure directory exists ---
    private static void ensureDirectoryExists() {
        File directory = new File(DATA_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    // --- STUDENT DATA METHODS ---
    public static void saveStudentData(List<Student> students) {
        ensureDirectoryExists();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(STUDENT_DATA_FILE))) {
            oos.writeObject(students);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Student> loadStudentData() {
        File file = new File(STUDENT_DATA_FILE);
        if (!file.exists())
            return new ArrayList<>(); // Return empty list if file doesn't exist

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(STUDENT_DATA_FILE))) {
            return (List<Student>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public static void registerStudent(Student student) {
        List<Student> students = loadStudentData();
        students.add(student);
        saveStudentData(students);
    }

    public static List<Student> getAllStudents() {
        return loadStudentData();
    }

    // --- ITEM METHODS ---
    public static void saveItems(List<Item> items) {
        ensureDirectoryExists();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ITEMS_FILE))) {
            oos.writeObject(items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Item> loadItems() {
        File file = new File(ITEMS_FILE);
        if (!file.exists())
            return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ITEMS_FILE))) {
            return (List<Item>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public static void addItem(Item item) {
        List<Item> items = loadItems();
        items.add(item);
        saveItems(items);
    }

    public static void updateItem(String id, Item updatedItem) {
        List<Item> items = loadItems();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(id)) {
                items.set(i, updatedItem);
                saveItems(items);
                break;
            }
        }
    }

    public static void deleteItem(String itemId) {
        List<Item> items = loadItems();
        items.removeIf(item -> item.getId().equals(itemId));
        saveItems(items);

        List<Notification> notifications = loadNotifications();
        notifications.removeIf(n -> n.getItemId().equals(itemId));
        saveNotifications(notifications);

        DashboardController.refreshBadge();
    }

    public static List<Item> getAllItems() {
        return loadItems();
    }

    // --- NOTIFICATION METHODS ---
    public static void saveNotifications(List<Notification> notifications) {
        ensureDirectoryExists();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NOTIFICATIONS_FILE))) {
            oos.writeObject(notifications);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Notification> loadNotifications() {
        File file = new File(NOTIFICATIONS_FILE);
        if (!file.exists())
            return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(NOTIFICATIONS_FILE))) {
            return (List<Notification>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public static void addNotification(Notification notification) {
        List<Notification> notifications = loadNotifications();
        notifications.add(notification);
        saveNotifications(notifications);
    }

    public static List<Notification> getAllNotifications() {
        return loadNotifications();
    }

    public static void saveAll() {
        saveItems(getAllItems());
        saveStudentData(getAllStudents());
        saveNotifications(getAllNotifications());
    }
}

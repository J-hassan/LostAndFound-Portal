package com.lostfound.storage;

import com.lostfound.models.FoundItem;
import com.lostfound.models.Item;
import com.lostfound.models.LostItem;
import com.lostfound.models.Notification;
import com.lostfound.models.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for FileManager.
 *
 * These tests use the real file-based storage (~/LostAndFoundData/).
 * Each test starts with empty stores and cleans up afterwards.
 */
class FileManagerTest {

    @BeforeEach
    void clearStorage() {
        FileManager.saveStudentData(new ArrayList<>());
        FileManager.saveItems(new ArrayList<>());
        FileManager.saveNotifications(new ArrayList<>());
    }

    @AfterEach
    void cleanUp() {
        FileManager.saveStudentData(new ArrayList<>());
        FileManager.saveItems(new ArrayList<>());
        FileManager.saveNotifications(new ArrayList<>());
    }

    // ===================== STUDENT TESTS =====================

    @Test
    void getAllStudents_whenEmpty_returnsEmptyList() {
        List<Student> students = FileManager.getAllStudents();
        assertTrue(students.isEmpty());
    }

    @Test
    void registerStudent_addsOneStudent() {
        FileManager.registerStudent(new Student("Alice", "alice@uni.edu", "pw"));
        List<Student> students = FileManager.getAllStudents();
        assertEquals(1, students.size());
        assertEquals("Alice", students.get(0).getName());
    }

    @Test
    void registerStudent_multipleStudents_allPersisted() {
        FileManager.registerStudent(new Student("Alice", "alice@uni.edu", "pw1"));
        FileManager.registerStudent(new Student("Bob", "bob@uni.edu", "pw2"));
        List<Student> students = FileManager.getAllStudents();
        assertEquals(2, students.size());
    }

    @Test
    void saveStudentData_replacesExistingList() {
        FileManager.registerStudent(new Student("Alice", "alice@uni.edu", "pw"));
        List<Student> replacement = new ArrayList<>();
        replacement.add(new Student("Bob", "bob@uni.edu", "pw2"));
        FileManager.saveStudentData(replacement);
        List<Student> students = FileManager.getAllStudents();
        assertEquals(1, students.size());
        assertEquals("Bob", students.get(0).getName());
    }

    // ===================== ITEM TESTS =====================

    @Test
    void getAllItems_whenEmpty_returnsEmptyList() {
        List<Item> items = FileManager.getAllItems();
        assertTrue(items.isEmpty());
    }

    @Test
    void addItem_lostItem_persistedAndRetrieved() {
        LostItem item = new LostItem("Wallet", "WALLET", "Brown wallet", "Library", "user@uni.edu");
        FileManager.addItem(item);

        List<Item> items = FileManager.getAllItems();
        assertEquals(1, items.size());
        assertEquals("Wallet", items.get(0).getName());
        assertEquals("Lost", items.get(0).getType());
    }

    @Test
    void addItem_foundItem_persistedAndRetrieved() {
        FoundItem item = new FoundItem("Keys", "KEYS", "Keychain", "Parking", "finder@uni.edu");
        FileManager.addItem(item);

        List<Item> items = FileManager.getAllItems();
        assertEquals(1, items.size());
        assertEquals("Keys", items.get(0).getName());
        assertEquals("Found", items.get(0).getType());
    }

    @Test
    void addItem_multipleItems_allPersisted() {
        FileManager.addItem(new LostItem("Wallet", "WALLET", "desc", "Lib", "a@b.com"));
        FileManager.addItem(new FoundItem("Phone", "ELECTRONICS", "desc", "Hall", "c@d.com"));
        assertEquals(2, FileManager.getAllItems().size());
    }

    @Test
    void updateItem_changesStoredItem() {
        LostItem original = new LostItem("Wallet", "WALLET", "desc", "Library", "user@uni.edu");
        FileManager.addItem(original);
        String id = original.getId();

        LostItem updated = new LostItem("Purse", "WALLET", "small purse", "Cafeteria", "user@uni.edu");
        updated.setId(id);
        FileManager.updateItem(id, updated);

        List<Item> items = FileManager.getAllItems();
        assertEquals(1, items.size());
        assertEquals("Purse", items.get(0).getName());
        assertEquals("Cafeteria", items.get(0).getLocation());
    }

    @Test
    void updateItem_withNonExistentId_doesNotChangeList() {
        LostItem item = new LostItem("Keys", "KEYS", "house keys", "Office", "a@b.com");
        FileManager.addItem(item);

        LostItem ghost = new LostItem("Ghost", "OTHER", "ghost", "Nowhere", "g@h.com");
        ghost.setId("non-existent-id");
        FileManager.updateItem("non-existent-id", ghost);

        List<Item> items = FileManager.getAllItems();
        assertEquals(1, items.size());
        assertEquals("Keys", items.get(0).getName());
    }

    @Test
    void deleteItem_removesItemFromStore() {
        LostItem item = new LostItem("Jacket", "CLOTHING", "blue jacket", "Gym", "a@b.com");
        FileManager.addItem(item);
        String id = item.getId();

        FileManager.deleteItem(id);

        assertTrue(FileManager.getAllItems().isEmpty());
    }

    @Test
    void deleteItem_alsoRemovesAssociatedNotifications() {
        LostItem item = new LostItem("Book", "OTHER", "textbook", "Lab", "a@b.com");
        FileManager.addItem(item);
        String itemId = item.getId();

        Notification n = new Notification(itemId, "Book", "Lab", "OTHER",
                "a@b.com", LocalDate.now(), "c@d.com");
        FileManager.addNotification(n);
        assertEquals(1, FileManager.getAllNotifications().size());

        FileManager.deleteItem(itemId);

        assertTrue(FileManager.getAllNotifications().isEmpty());
    }

    @Test
    void deleteItem_withNonExistentId_doesNotChangeList() {
        LostItem item = new LostItem("Pen", "OTHER", "blue pen", "Classroom", "a@b.com");
        FileManager.addItem(item);

        FileManager.deleteItem("non-existent-id");

        assertEquals(1, FileManager.getAllItems().size());
    }

    // ===================== NOTIFICATION TESTS =====================

    @Test
    void getAllNotifications_whenEmpty_returnsEmptyList() {
        assertTrue(FileManager.getAllNotifications().isEmpty());
    }

    @Test
    void addNotification_persistedAndRetrieved() {
        Notification n = new Notification("item-1", "Wallet", "Library", "WALLET",
                "reporter@uni.edu", LocalDate.now(), "receiver@uni.edu");
        FileManager.addNotification(n);

        List<Notification> notifications = FileManager.getAllNotifications();
        assertEquals(1, notifications.size());
        assertEquals("Wallet", notifications.get(0).getName());
        assertEquals("receiver@uni.edu", notifications.get(0).getReceiverEmail());
    }

    @Test
    void addNotification_multiple_allPersisted() {
        FileManager.addNotification(new Notification("i1", "Wallet", "Lib", "WALLET",
                "a@b.com", LocalDate.now(), "r1@uni.edu"));
        FileManager.addNotification(new Notification("i2", "Keys", "Park", "KEYS",
                "c@d.com", LocalDate.now(), "r2@uni.edu"));

        assertEquals(2, FileManager.getAllNotifications().size());
    }

    @Test
    void saveNotifications_replacesExistingList() {
        FileManager.addNotification(new Notification("i1", "Wallet", "Lib", "WALLET",
                "a@b.com", LocalDate.now(), "r@uni.edu"));

        Notification replacement = new Notification("i2", "Phone", "Hall", "ELECTRONICS",
                "x@y.com", LocalDate.now(), "z@uni.edu");
        List<Notification> replacements = new ArrayList<>();
        replacements.add(replacement);
        FileManager.saveNotifications(replacements);

        List<Notification> notifications = FileManager.getAllNotifications();
        assertEquals(1, notifications.size());
        assertEquals("Phone", notifications.get(0).getName());
    }

    // ===================== saveAll =====================

    @Test
    void saveAll_doesNotCorruptExistingData() {
        FileManager.registerStudent(new Student("Eve", "eve@uni.edu", "pw"));
        FileManager.addItem(new LostItem("Bag", "OTHER", "backpack", "Hall", "eve@uni.edu"));

        FileManager.saveAll();

        assertEquals(1, FileManager.getAllStudents().size());
        assertEquals(1, FileManager.getAllItems().size());
    }
}

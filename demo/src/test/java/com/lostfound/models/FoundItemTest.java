package com.lostfound.models;

import com.lostfound.models.enums.Status;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FoundItemTest {

    @Test
    void constructor_storesAllFields() {
        FoundItem item = new FoundItem("Wallet", "WALLET", "Black wallet", "Cafeteria", "finder@uni.edu");
        assertEquals("Wallet", item.getName());
        assertEquals("WALLET", item.getCategory());
        assertEquals("Black wallet", item.getDescription());
        assertEquals("Cafeteria", item.getLocation());
        assertEquals("finder@uni.edu", item.getReportedByEmail());
    }

    @Test
    void getType_returnsFound() {
        FoundItem item = new FoundItem("Keys", "KEYS", "Car keys", "Parking", "f@uni.edu");
        assertEquals("Found", item.getType());
    }

    @Test
    void defaultStatus_isOpen() {
        FoundItem item = new FoundItem("Phone", "ELECTRONICS", "iPhone", "Lab", "x@y.com");
        assertEquals(Status.OPEN, item.getStatus());
    }

    @Test
    void setStatus_updatesToClosed() {
        FoundItem item = new FoundItem("Jacket", "CLOTHING", "Red jacket", "Gym", "a@b.com");
        item.setStatus(Status.CLOSED);
        assertEquals(Status.CLOSED, item.getStatus());
    }

    @Test
    void setStatus_updatesToClaimed() {
        FoundItem item = new FoundItem("ID Card", "IDCARD", "Staff ID", "Office", "a@b.com");
        item.setStatus(Status.CLAIMED);
        assertEquals(Status.CLAIMED, item.getStatus());
    }

    @Test
    void getId_isNotNull() {
        FoundItem item = new FoundItem("Bag", "OTHER", "Backpack", "Library", "a@b.com");
        assertNotNull(item.getId());
        assertFalse(item.getId().isEmpty());
    }

    @Test
    void setId_changesId() {
        FoundItem item = new FoundItem("Scarf", "CLOTHING", "Blue scarf", "Hallway", "a@b.com");
        item.setId("found-id-456");
        assertEquals("found-id-456", item.getId());
    }

    @Test
    void setDate_changesDate() {
        FoundItem item = new FoundItem("Glasses", "OTHER", "Reading glasses", "Canteen", "a@b.com");
        LocalDate newDate = LocalDate.of(2024, 3, 20);
        item.setDate(newDate);
        assertEquals(newDate, item.getDate());
    }

    @Test
    void getDate_defaultsToToday() {
        FoundItem item = new FoundItem("Watch", "ELECTRONICS", "Silver watch", "Sports hall", "a@b.com");
        assertEquals(LocalDate.now(), item.getDate());
    }

    @Test
    void twoItems_haveDifferentIds() {
        FoundItem item1 = new FoundItem("Book", "OTHER", "Textbook", "Lab", "a@b.com");
        FoundItem item2 = new FoundItem("Book", "OTHER", "Textbook", "Lab", "a@b.com");
        assertNotEquals(item1.getId(), item2.getId());
    }
}

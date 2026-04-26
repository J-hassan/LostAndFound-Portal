package com.lostfound.models;

import com.lostfound.models.enums.Status;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LostItemTest {

    @Test
    void constructor_storesAllFields() {
        LostItem item = new LostItem("Wallet", "WALLET", "Brown wallet", "Library", "user@uni.edu");
        assertEquals("Wallet", item.getName());
        assertEquals("WALLET", item.getCategory());
        assertEquals("Brown wallet", item.getDescription());
        assertEquals("Library", item.getLocation());
        assertEquals("user@uni.edu", item.getReportedByEmail());
    }

    @Test
    void getType_returnsLost() {
        LostItem item = new LostItem("Keys", "KEYS", "House keys", "Cafeteria", "a@b.com");
        assertEquals("Lost", item.getType());
    }

    @Test
    void defaultStatus_isOpen() {
        LostItem item = new LostItem("Phone", "ELECTRONICS", "Black phone", "Hall", "x@y.com");
        assertEquals(Status.OPEN, item.getStatus());
    }

    @Test
    void setStatus_updatesToClosed() {
        LostItem item = new LostItem("Jacket", "CLOTHING", "Blue jacket", "Gym", "a@b.com");
        item.setStatus(Status.CLOSED);
        assertEquals(Status.CLOSED, item.getStatus());
    }

    @Test
    void setStatus_updatesToClaimed() {
        LostItem item = new LostItem("ID Card", "IDCARD", "Student ID", "Canteen", "a@b.com");
        item.setStatus(Status.CLAIMED);
        assertEquals(Status.CLAIMED, item.getStatus());
    }

    @Test
    void getId_isNotNull() {
        LostItem item = new LostItem("Book", "OTHER", "Textbook", "Lab", "a@b.com");
        assertNotNull(item.getId());
        assertFalse(item.getId().isEmpty());
    }

    @Test
    void setId_changesId() {
        LostItem item = new LostItem("Bag", "OTHER", "Black bag", "Parking", "a@b.com");
        item.setId("custom-id-123");
        assertEquals("custom-id-123", item.getId());
    }

    @Test
    void setDate_changesDate() {
        LostItem item = new LostItem("Umbrella", "OTHER", "Red umbrella", "Bus stop", "a@b.com");
        LocalDate newDate = LocalDate.of(2024, 6, 15);
        item.setDate(newDate);
        assertEquals(newDate, item.getDate());
    }

    @Test
    void getDate_defaultsToToday() {
        LostItem item = new LostItem("Cup", "OTHER", "Mug", "Kitchen", "a@b.com");
        assertEquals(LocalDate.now(), item.getDate());
    }
}

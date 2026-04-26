package com.lostfound.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    private static final LocalDate TEST_DATE = LocalDate.of(2024, 5, 10);

    @Test
    void constructor_storesAllFields() {
        Notification n = new Notification("item-001", "Wallet", "Library", "WALLET",
                "reporter@uni.edu", TEST_DATE, "receiver@uni.edu");

        assertEquals("item-001", n.getItemId());
        assertEquals("Wallet", n.getName());
        assertEquals("Library", n.getLocation());
        assertEquals("WALLET", n.getCategory());
        assertEquals("reporter@uni.edu", n.getReportedByEmail());
        assertEquals(TEST_DATE, n.getDate());
        assertEquals("receiver@uni.edu", n.getReceiverEmail());
    }

    @Test
    void getId_startsWithNotifPrefix() {
        Notification n = new Notification("item-002", "Keys", "Parking", "KEYS",
                "a@b.com", TEST_DATE, "c@d.com");
        assertNotNull(n.getId());
        assertTrue(n.getId().startsWith("NOTIF-"),
                "Notification ID should start with 'NOTIF-' but was: " + n.getId());
    }

    @Test
    void twoNotifications_haveDifferentIds() {
        Notification n1 = new Notification("item-1", "Phone", "Hall", "ELECTRONICS",
                "a@b.com", TEST_DATE, "c@d.com");
        Notification n2 = new Notification("item-1", "Phone", "Hall", "ELECTRONICS",
                "a@b.com", TEST_DATE, "c@d.com");
        assertNotEquals(n1.getId(), n2.getId());
    }

    @Test
    void getName_returnsName() {
        Notification n = new Notification("x", "Laptop", "Lab", "ELECTRONICS",
                "e@f.com", TEST_DATE, "g@h.com");
        assertEquals("Laptop", n.getName());
    }

    @Test
    void getLocation_returnsLocation() {
        Notification n = new Notification("x", "Cup", "Canteen", "OTHER",
                "e@f.com", TEST_DATE, "g@h.com");
        assertEquals("Canteen", n.getLocation());
    }

    @Test
    void getCategory_returnsCategory() {
        Notification n = new Notification("x", "Jacket", "Gym", "CLOTHING",
                "e@f.com", TEST_DATE, "g@h.com");
        assertEquals("CLOTHING", n.getCategory());
    }

    @Test
    void getDate_returnsDate() {
        Notification n = new Notification("x", "Watch", "Office", "ELECTRONICS",
                "e@f.com", TEST_DATE, "g@h.com");
        assertEquals(TEST_DATE, n.getDate());
    }

    @Test
    void getReportedByEmail_returnsEmail() {
        Notification n = new Notification("x", "Pen", "Classroom", "OTHER",
                "reporter@uni.edu", TEST_DATE, "r@uni.edu");
        assertEquals("reporter@uni.edu", n.getReportedByEmail());
    }

    @Test
    void getReceiverEmail_returnsEmail() {
        Notification n = new Notification("x", "Book", "Library", "OTHER",
                "a@b.com", TEST_DATE, "receiver@uni.edu");
        assertEquals("receiver@uni.edu", n.getReceiverEmail());
    }
}

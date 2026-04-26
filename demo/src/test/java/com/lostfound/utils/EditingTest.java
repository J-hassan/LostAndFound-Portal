package com.lostfound.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EditingTest {

    @BeforeEach
    void setUp() {
        Editing.clear();
        Editing.setEditingMode(false);
    }

    // --- Singleton ---

    @Test
    void getInstance_returnsSameInstance() {
        Editing e1 = Editing.getInstance();
        Editing e2 = Editing.getInstance();
        assertSame(e1, e2);
    }

    @Test
    void getInstance_returnsNonNull() {
        assertNotNull(Editing.getInstance());
    }

    // --- setEditingItems / getters ---

    @Test
    void setEditingItems_thenGetters_returnCorrectValues() {
        LocalDate date = LocalDate.of(2024, 8, 20);
        Editing.setEditingItems("id-001", "Wallet", "WALLET", "Lost",
                "Library", "Brown leather wallet", date);

        Editing instance = Editing.getInstance();
        assertEquals("id-001", instance.getId());
        assertEquals("Wallet", instance.getName());
        assertEquals("WALLET", instance.getCategory());
        assertEquals("Lost", instance.getType());
        assertEquals("Library", instance.getLocation());
        assertEquals("Brown leather wallet", instance.getDescription());
        assertEquals(date, instance.getDate());
    }

    @Test
    void setEditingItems_overwritesPreviousValues() {
        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalDate date2 = LocalDate.of(2024, 12, 31);

        Editing.setEditingItems("id-A", "Keys", "KEYS", "Lost", "Parking", "Keychain", date1);
        Editing.setEditingItems("id-B", "Phone", "ELECTRONICS", "Found", "Hall", "Smartphone", date2);

        Editing instance = Editing.getInstance();
        assertEquals("id-B", instance.getId());
        assertEquals("Phone", instance.getName());
        assertEquals("ELECTRONICS", instance.getCategory());
        assertEquals("Found", instance.getType());
        assertEquals("Hall", instance.getLocation());
        assertEquals("Smartphone", instance.getDescription());
        assertEquals(date2, instance.getDate());
    }

    // --- Editing mode ---

    @Test
    void isEditingMode_defaultsFalse() {
        assertFalse(Editing.isEditingMode());
    }

    @Test
    void setEditingMode_true_returnsTrue() {
        Editing.setEditingMode(true);
        assertTrue(Editing.isEditingMode());
    }

    @Test
    void setEditingMode_false_returnsFalse() {
        Editing.setEditingMode(true);
        Editing.setEditingMode(false);
        assertFalse(Editing.isEditingMode());
    }

    // --- clear ---

    @Test
    void clear_resetsAllFieldsToNull() {
        LocalDate date = LocalDate.of(2024, 5, 5);
        Editing.setEditingItems("id-x", "Item", "OTHER", "Lost", "Loc", "Desc", date);
        Editing.clear();

        Editing instance = Editing.getInstance();
        assertNull(instance.getId());
        assertNull(instance.getName());
        assertNull(instance.getCategory());
        assertNull(instance.getType());
        assertNull(instance.getLocation());
        assertNull(instance.getDescription());
        assertNull(instance.getDate());
    }

    @Test
    void clear_doesNotResetEditingMode() {
        Editing.setEditingMode(true);
        Editing.clear();
        // editingMode is a separate flag; clear() should not change it
        assertTrue(Editing.isEditingMode());
    }
}

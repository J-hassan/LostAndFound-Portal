package com.lostfound.models.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void values_containsIdcard() {
        assertNotNull(Category.IDCARD);
    }

    @Test
    void values_containsWallet() {
        assertNotNull(Category.WALLET);
    }

    @Test
    void values_containsKeys() {
        assertNotNull(Category.KEYS);
    }

    @Test
    void values_containsElectronics() {
        assertNotNull(Category.ELECTRONICS);
    }

    @Test
    void values_containsClothing() {
        assertNotNull(Category.CLOTHING);
    }

    @Test
    void values_containsOther() {
        assertNotNull(Category.OTHER);
    }

    @Test
    void values_hasExactlySixEntries() {
        assertEquals(6, Category.values().length);
    }

    @Test
    void valueOf_each() {
        assertEquals(Category.IDCARD, Category.valueOf("IDCARD"));
        assertEquals(Category.WALLET, Category.valueOf("WALLET"));
        assertEquals(Category.KEYS, Category.valueOf("KEYS"));
        assertEquals(Category.ELECTRONICS, Category.valueOf("ELECTRONICS"));
        assertEquals(Category.CLOTHING, Category.valueOf("CLOTHING"));
        assertEquals(Category.OTHER, Category.valueOf("OTHER"));
    }

    @Test
    void valueOf_invalidName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Category.valueOf("FOOD"));
    }
}

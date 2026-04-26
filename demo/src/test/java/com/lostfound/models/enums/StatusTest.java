package com.lostfound.models.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusTest {

    @Test
    void values_containsOpen() {
        assertNotNull(Status.OPEN);
    }

    @Test
    void values_containsClosed() {
        assertNotNull(Status.CLOSED);
    }

    @Test
    void values_containsClaimed() {
        assertNotNull(Status.CLAIMED);
    }

    @Test
    void values_hasExactlyThreeEntries() {
        assertEquals(3, Status.values().length);
    }

    @Test
    void valueOf_open() {
        assertEquals(Status.OPEN, Status.valueOf("OPEN"));
    }

    @Test
    void valueOf_closed() {
        assertEquals(Status.CLOSED, Status.valueOf("CLOSED"));
    }

    @Test
    void valueOf_claimed() {
        assertEquals(Status.CLAIMED, Status.valueOf("CLAIMED"));
    }

    @Test
    void valueOf_invalidName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Status.valueOf("UNKNOWN"));
    }
}

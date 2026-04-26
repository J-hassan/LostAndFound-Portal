package com.lostfound.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionManagerTest {

    @BeforeEach
    void setUp() {
        SessionManager.clearSession();
        SessionManager.clearCredentials();
    }

    // --- Current user email ---

    @Test
    void setCurrentUserEmail_thenGet_returnsEmail() {
        SessionManager.setCurrentUserEmail("alice@uni.edu");
        assertEquals("alice@uni.edu", SessionManager.getCurrentUserEmail());
    }

    @Test
    void getCurrentUserEmail_beforeSet_returnsNull() {
        assertNull(SessionManager.getCurrentUserEmail());
    }

    // --- Current user name ---

    @Test
    void setCurrentUserName_thenGet_returnsName() {
        SessionManager.setCurrentUserName("Alice");
        assertEquals("Alice", SessionManager.getCurrentUserName());
    }

    @Test
    void getCurrentUserName_beforeSet_returnsNull() {
        assertNull(SessionManager.getCurrentUserName());
    }

    // --- clearSession ---

    @Test
    void clearSession_resetsEmailAndName() {
        SessionManager.setCurrentUserEmail("bob@uni.edu");
        SessionManager.setCurrentUserName("Bob");
        SessionManager.clearSession();
        assertNull(SessionManager.getCurrentUserEmail());
        assertNull(SessionManager.getCurrentUserName());
    }

    // --- Temporary credentials ---

    @Test
    void setCredentials_thenGet_returnsBoth() {
        SessionManager.setCredentials("carol@uni.edu", "pass123");
        assertEquals("carol@uni.edu", SessionManager.getTempUserEmail());
        assertEquals("pass123", SessionManager.getTempUserPassword());
    }

    @Test
    void getTempUserEmail_beforeSet_returnsNull() {
        assertNull(SessionManager.getTempUserEmail());
    }

    @Test
    void getTempUserPassword_beforeSet_returnsNull() {
        assertNull(SessionManager.getTempUserPassword());
    }

    @Test
    void clearCredentials_resetsTempEmailAndPassword() {
        SessionManager.setCredentials("dave@uni.edu", "secret");
        SessionManager.clearCredentials();
        assertNull(SessionManager.getTempUserEmail());
        assertNull(SessionManager.getTempUserPassword());
    }

    // --- Independent state ---

    @Test
    void sessionAndCredentials_areIndependent() {
        SessionManager.setCurrentUserEmail("main@uni.edu");
        SessionManager.setCredentials("temp@uni.edu", "pw");
        SessionManager.clearCredentials();
        // clearing credentials should not affect the current session
        assertEquals("main@uni.edu", SessionManager.getCurrentUserEmail());
    }

    @Test
    void clearSession_doesNotClearCredentials() {
        SessionManager.setCredentials("temp@uni.edu", "pw");
        SessionManager.setCurrentUserEmail("main@uni.edu");
        SessionManager.clearSession();
        assertEquals("temp@uni.edu", SessionManager.getTempUserEmail());
    }
}

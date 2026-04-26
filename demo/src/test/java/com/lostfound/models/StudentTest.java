package com.lostfound.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void constructor_storesAllFields() {
        Student student = new Student("Alice", "alice@example.com", "secret");
        assertEquals("Alice", student.getName());
        assertEquals("alice@example.com", student.getEmail());
        assertEquals("secret", student.getPassword());
    }

    @Test
    void getName_returnsName() {
        Student student = new Student("Bob", "bob@uni.edu", "pass123");
        assertEquals("Bob", student.getName());
    }

    @Test
    void getEmail_returnsEmail() {
        Student student = new Student("Carol", "carol@uni.edu", "pw");
        assertEquals("carol@uni.edu", student.getEmail());
    }

    @Test
    void getPassword_returnsPassword() {
        Student student = new Student("Dan", "dan@uni.edu", "mypassword");
        assertEquals("mypassword", student.getPassword());
    }

    @Test
    void constructor_allowsNullFields() {
        Student student = new Student(null, null, null);
        assertNull(student.getName());
        assertNull(student.getEmail());
        assertNull(student.getPassword());
    }
}

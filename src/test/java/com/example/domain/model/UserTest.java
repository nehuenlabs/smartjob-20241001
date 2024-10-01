package com.example.domain.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserCreation() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .email("john@example.com")
                .password("securePassword123")
                .phones(new ArrayList<>())
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .token("someToken")
                .isActive(true)
                .build();

        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john@example.com", user.getEmail());
        assertNotNull(user.getPassword());
        assertTrue(user.getPhones().isEmpty());
        assertNotNull(user.getCreated());
        assertNotNull(user.getModified());
        assertNotNull(user.getLastLogin());
        assertEquals("someToken", user.getToken());
        assertTrue(user.isActive());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        User user1 = User.builder().id(id).name("John").email("john@example.com").build();
        User user2 = User.builder().id(id).name("John").email("john@example.com").build();
        User user3 = User.builder().id(UUID.randomUUID()).name("Jane").email("jane@example.com").build();

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }

    @Test
    void testToString() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .email("john@example.com")
                .build();

        String userString = user.toString();
        assertTrue(userString.contains("John Doe"));
        assertTrue(userString.contains("john@example.com"));
    }
}
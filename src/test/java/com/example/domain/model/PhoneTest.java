package com.example.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhoneTest {

    @Test
    void testPhoneCreation() {
        Phone phone = Phone.builder()
                .id(1L)
                .number("1234567890")
                .citycode("123")
                .countrycode("1")
                .build();

        assertNotNull(phone);
        assertEquals(1L, phone.getId());
        assertEquals("1234567890", phone.getNumber());
        assertEquals("123", phone.getCitycode());
        assertEquals("1", phone.getCountrycode());
    }

    @Test
    void testEqualsAndHashCode() {
        Phone phone1 = Phone.builder().id(1L).number("1234567890").citycode("123").countrycode("1").build();
        Phone phone2 = Phone.builder().id(1L).number("1234567890").citycode("123").countrycode("1").build();
        Phone phone3 = Phone.builder().id(2L).number("0987654321").citycode("321").countrycode("2").build();

        assertEquals(phone1, phone2);
        assertNotEquals(phone1, phone3);
        assertEquals(phone1.hashCode(), phone2.hashCode());
        assertNotEquals(phone1.hashCode(), phone3.hashCode());
    }

    @Test
    void testToString() {
        Phone phone = Phone.builder()
                .id(1L)
                .number("1234567890")
                .citycode("123")
                .countrycode("1")
                .build();

        String phoneString = phone.toString();
        assertTrue(phoneString.contains("1234567890"));
        assertTrue(phoneString.contains("123"));
        assertTrue(phoneString.contains("1"));
    }
}
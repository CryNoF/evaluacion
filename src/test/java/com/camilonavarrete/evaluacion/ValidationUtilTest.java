package com.camilonavarrete.evaluacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.camilonavarrete.evaluacion.utils.ValidationUtil;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {

    private ValidationUtil validationUtil;

    @BeforeEach
    void setUp() {
        validationUtil = new ValidationUtil();
        ReflectionTestUtils.setField(validationUtil, "emailRegex", 
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        ReflectionTestUtils.setField(validationUtil, "passwordRegex", 
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$");
        ReflectionTestUtils.setField(validationUtil, "passwordMessage", 
            "Password must have at least 8 characters, one uppercase, one lowercase and one number");
    }

    @Test
    void validEmails() {
        assertTrue(validationUtil.isValidEmail("user@domain.com"));
        assertTrue(validationUtil.isValidEmail("test.user@example.org"));
        assertTrue(validationUtil.isValidEmail("user123@test.cl"));
        assertTrue(validationUtil.isValidEmail("juan@rodriguez.org"));
    }

    @Test
    void invalidEmails() {
        assertFalse(validationUtil.isValidEmail("invalid-email"));
        assertFalse(validationUtil.isValidEmail("user@"));
        assertFalse(validationUtil.isValidEmail("@domain.com"));
        assertFalse(validationUtil.isValidEmail("user.domain.com"));
        assertFalse(validationUtil.isValidEmail(""));
        assertFalse(validationUtil.isValidEmail(null));
        assertFalse(validationUtil.isValidEmail("   "));
    }

    @Test
    void validPasswords() {
        assertTrue(validationUtil.isValidPassword("Password123"));
        assertTrue(validationUtil.isValidPassword("Hunter123"));
        assertTrue(validationUtil.isValidPassword("MyPass456"));
        assertTrue(validationUtil.isValidPassword("Test1234"));
    }

    @Test
    void invalidPasswords() {
        assertFalse(validationUtil.isValidPassword("password123"));
        assertFalse(validationUtil.isValidPassword("PASSWORD123"));
        assertFalse(validationUtil.isValidPassword("Password"));
        assertFalse(validationUtil.isValidPassword("Pass1"));
        assertFalse(validationUtil.isValidPassword(""));
        assertFalse(validationUtil.isValidPassword(null));
        assertFalse(validationUtil.isValidPassword("   "));
    }

    @Test
    void passwordValidationMessage() {
        String message = validationUtil.getPasswordValidationMessage();
        assertEquals("Password must have at least 8 characters, one uppercase, one lowercase and one number", 
                     message);
    }
}
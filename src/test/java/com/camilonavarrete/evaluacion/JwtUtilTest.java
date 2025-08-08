package com.camilonavarrete.evaluacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.camilonavarrete.evaluacion.utils.JwtUtil;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String testEmail = "test@gmail.com";
    private final String testSecret = "testKey123456789012345678901234567890";
    private final Long testExpiration = 86400000L; 

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", testSecret);
        ReflectionTestUtils.setField(jwtUtil, "expiration", testExpiration);
    }

    @Test
    void generateTokenSuccess() {

        String token = jwtUtil.generateToken(testEmail);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    void extractUsernameSuccess() {

        String token = jwtUtil.generateToken(testEmail);

        String extractedEmail = jwtUtil.extractUsername(token);

        assertEquals(testEmail, extractedEmail);
    }

    @Test
    void extractExpirationSuccess() {
        String token = jwtUtil.generateToken(testEmail);
        Date beforeGeneration = new Date();

        Date expiration = jwtUtil.extractExpiration(token);

        assertNotNull(expiration);
        assertTrue(expiration.after(beforeGeneration));
        assertTrue(expiration.before(new Date(System.currentTimeMillis() + testExpiration + 1000)));
    }

    @Test
    void validateTokenSuccess() {
        String token = jwtUtil.generateToken(testEmail);

        Boolean isValid = jwtUtil.validateToken(token, testEmail);

        assertTrue(isValid);
    }

    @Test
    void validateTokenWrongEmail() {
        String token = jwtUtil.generateToken(testEmail);

        Boolean isValid = jwtUtil.validateToken(token, "wrong@example.com");

        assertFalse(isValid);
    }

    @Test
    void validateTokenExpired() {
        JwtUtil shortExpirationJwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(shortExpirationJwtUtil, "secret", testSecret);
        ReflectionTestUtils.setField(shortExpirationJwtUtil, "expiration", 1L);
        
        String token = shortExpirationJwtUtil.generateToken(testEmail);
        
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Boolean isValid = shortExpirationJwtUtil.validateToken(token, testEmail);

        assertFalse(isValid);
    }
}
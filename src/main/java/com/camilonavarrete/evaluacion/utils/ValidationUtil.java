package com.camilonavarrete.evaluacion.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ValidationUtil {

    @Value("${app.email-regex}")
    private String emailRegex;

    @Value("${app.password-regex}")
    private String passwordRegex;

    @Value("${app.password-message}")
    private String passwordMessage;

    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    public boolean isValidPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        return Pattern.compile(passwordRegex).matcher(password).matches();
    }

    public String getPasswordValidationMessage() {
        return passwordMessage;
    }
}
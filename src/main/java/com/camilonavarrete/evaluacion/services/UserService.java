package com.camilonavarrete.evaluacion.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.camilonavarrete.evaluacion.dto.requests.UserRequestDto;
import com.camilonavarrete.evaluacion.dto.responses.UserResponseDto;
import com.camilonavarrete.evaluacion.entities.User;
import com.camilonavarrete.evaluacion.exceptions.EmailAlreadyExistsException;
import com.camilonavarrete.evaluacion.exceptions.InvalidEmailFormatException;
import com.camilonavarrete.evaluacion.exceptions.InvalidPasswordFormatException;
import com.camilonavarrete.evaluacion.exceptions.ValidationException;
import com.camilonavarrete.evaluacion.mappers.UserMapper;
import com.camilonavarrete.evaluacion.repository.UserRepository;
import com.camilonavarrete.evaluacion.utils.JwtUtil;
import com.camilonavarrete.evaluacion.utils.ValidationUtil;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final ValidationUtil validationUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        log.info("Creating user with email: {}", userRequestDto.getEmail());

        validateUserRequest(userRequestDto);

        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = userMapper.toEntity(userRequestDto);
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        LocalDateTime now = LocalDateTime.now();
        user.setLastLogin(now);
        String token = jwtUtil.generateToken(user.getEmail());
        user.setToken(token);
        User savedUser = userRepository.saveAndFlush(user);
        log.info("User created successfully with ID: {}", savedUser.getId());

        savedUser = userRepository.findById(savedUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found after creation"));

        return userMapper.toResponseDto(savedUser);
    }

    private void validateUserRequest(UserRequestDto userRequestDto) {
        if (userRequestDto.getName() == null || userRequestDto.getName().trim().isEmpty()) {
            throw new ValidationException("Name is required");
        }

        if (userRequestDto.getEmail() == null || userRequestDto.getEmail().trim().isEmpty()) {
            throw new ValidationException("Email is required");
        }

        if (userRequestDto.getPassword() == null || userRequestDto.getPassword().trim().isEmpty()) {
            throw new ValidationException("Password is required");
        }

        if (!validationUtil.isValidEmail(userRequestDto.getEmail())) {
            throw new InvalidEmailFormatException("Invalid email format");
        }

        if (!validationUtil.isValidPassword(userRequestDto.getPassword())) {
            throw new InvalidPasswordFormatException(validationUtil.getPasswordValidationMessage());
        }
    }
}
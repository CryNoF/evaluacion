package com.camilonavarrete.evaluacion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.camilonavarrete.evaluacion.dto.PhoneDto;
import com.camilonavarrete.evaluacion.dto.requests.UserRequestDto;
import com.camilonavarrete.evaluacion.dto.responses.UserResponseDto;
import com.camilonavarrete.evaluacion.entities.User;
import com.camilonavarrete.evaluacion.exceptions.EmailAlreadyExistsException;
import com.camilonavarrete.evaluacion.exceptions.InvalidEmailFormatException;
import com.camilonavarrete.evaluacion.exceptions.InvalidPasswordFormatException;
import com.camilonavarrete.evaluacion.exceptions.ValidationException;
import com.camilonavarrete.evaluacion.mappers.UserMapper;
import com.camilonavarrete.evaluacion.repository.UserRepository;
import com.camilonavarrete.evaluacion.services.UserService;
import com.camilonavarrete.evaluacion.utils.JwtUtil;
import com.camilonavarrete.evaluacion.utils.ValidationUtil;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private ValidationUtil validationUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserRequestDto userRequestDto;
    private User user;
    private UserResponseDto userResponseDto;

    @BeforeEach
    void setUp() {
        PhoneDto phoneDto = new PhoneDto("12345678", "2", "58");
        userRequestDto = new UserRequestDto(
                "Camilo Navarrete",
                "camilonavarrete@gmail.com",
                "evaluacion123",
                List.of(phoneDto)
        );

        user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Camilo Navarrete");
        user.setEmail("camilonavarrete@gmail.com");
        user.setPassword("encodedPassword");
        user.setCreated(LocalDateTime.now());
        user.setModified(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setToken("jwt-token");
        user.setIsActive(true);

        userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setName(user.getName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setCreated(user.getCreated());
        userResponseDto.setModified(user.getModified());
        userResponseDto.setLastLogin(user.getLastLogin());
        userResponseDto.setToken(user.getToken());
        userResponseDto.setIsActive(user.getIsActive());
        userResponseDto.setPhones(List.of(phoneDto));
    }

    @Test
    void createUserSuccess() {
        when(validationUtil.isValidEmail(anyString())).thenReturn(true);
        when(validationUtil.isValidPassword(anyString())).thenReturn(true);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userMapper.toEntity(any(UserRequestDto.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtUtil.generateToken(anyString())).thenReturn("jwt-token");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponseDto(any(User.class))).thenReturn(userResponseDto);

        UserResponseDto result = userService.createUser(userRequestDto);

        assertNotNull(result);
        assertEquals("Camilo Navarrete", result.getName());
        assertEquals("camilonavarrete@gmail.com", result.getEmail());
        assertEquals("jwt-token", result.getToken());
        assertTrue(result.getIsActive());
    }

    @Test
    void emailAlreadyExistsException() {
        when(validationUtil.isValidEmail(anyString())).thenReturn(true);
        when(validationUtil.isValidPassword(anyString())).thenReturn(true);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        EmailAlreadyExistsException exception = assertThrows(
                EmailAlreadyExistsException.class,
                () -> userService.createUser(userRequestDto)
        );
        assertEquals("Email already exists", exception.getMessage());
    }

    @Test
    void InvalidEmailFormatException() {
        when(validationUtil.isValidEmail(anyString())).thenReturn(false);

        InvalidEmailFormatException exception = assertThrows(
                InvalidEmailFormatException.class,
                () -> userService.createUser(userRequestDto)
        );
        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    void invalidPasswordFormatException() {
        when(validationUtil.isValidEmail(anyString())).thenReturn(true);
        when(validationUtil.isValidPassword(anyString())).thenReturn(false);
        when(validationUtil.getPasswordValidationMessage()).thenReturn("Invalid password format");
        InvalidPasswordFormatException exception = assertThrows(
                InvalidPasswordFormatException.class,
                () -> userService.createUser(userRequestDto)
        );
        assertEquals("Invalid password format", exception.getMessage());
    }

    @Test
    void emptyNameException() {
        userRequestDto.setName("");

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.createUser(userRequestDto)
        );
        assertEquals("Name is required", exception.getMessage());
    }

    @Test
    void nullEmailException() {
        userRequestDto.setEmail(null);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.createUser(userRequestDto)
        );
        assertEquals("Email is required", exception.getMessage());
    }

    @Test
    void nullPasswordException() {
        userRequestDto.setPassword(null);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.createUser(userRequestDto)
        );
        assertEquals("Password is required", exception.getMessage());
    }
}

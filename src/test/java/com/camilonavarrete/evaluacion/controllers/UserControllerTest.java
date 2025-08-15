package com.camilonavarrete.evaluacion;

import com.camilonavarrete.evaluacion.controllers.UserController;
import com.camilonavarrete.evaluacion.dto.PhoneDto;
import com.camilonavarrete.evaluacion.dto.requests.UserRequestDto;
import com.camilonavarrete.evaluacion.dto.responses.UserResponseDto;
import com.camilonavarrete.evaluacion.exceptions.EmailAlreadyExistsException;
import com.camilonavarrete.evaluacion.exceptions.GlobalExceptionHandler;
import com.camilonavarrete.evaluacion.exceptions.InvalidEmailFormatException;
import com.camilonavarrete.evaluacion.exceptions.InvalidPasswordFormatException;
import com.camilonavarrete.evaluacion.exceptions.ValidationException;
import com.camilonavarrete.evaluacion.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private UserRequestDto createValidUserRequestDto() {
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setName("Camilo Navarrete");
        requestDto.setEmail("camilonavarrete@gmail.com");
        requestDto.setPassword("evaluacion123");
        requestDto.setPhones(Arrays.asList(createPhoneDto()));
        return requestDto;
    }

    private PhoneDto createPhoneDto() {
        return new PhoneDto("123456789", "9", "56");
    }

    private UserResponseDto createUserResponseDto() {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(UUID.randomUUID());
        responseDto.setName("Camilo Navarrete");
        responseDto.setEmail("camilonavarrete@gmail.com");
        responseDto.setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9");
        responseDto.setCreated(LocalDateTime.now());
        responseDto.setModified(LocalDateTime.now());
        responseDto.setLastLogin(LocalDateTime.now());
        responseDto.setPhones(Arrays.asList(createPhoneDto()));
        responseDto.setIsActive(true);
        return responseDto;
    }


    @Test
    void handlePhoneScenarios() throws Exception {
        UserRequestDto requestDto = createValidUserRequestDto();
        requestDto.setPhones(Arrays.asList(
                new PhoneDto("123456789", "9", "56"),
                new PhoneDto("987654321", "2", "56")
        ));
        
        UserResponseDto responseDto = createUserResponseDto();
        responseDto.setPhones(Arrays.asList(
                new PhoneDto("123456789", "9", "56"),
                new PhoneDto("987654321", "2", "56")
        ));

        when(userService.createUser(any(UserRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.phones").isArray())
                .andExpect(jsonPath("$.phones", hasSize(2)))
                .andExpect(jsonPath("$.phones[0].number").value("123456789"))
                .andExpect(jsonPath("$.phones[1].number").value("987654321"));

        requestDto.setPhones(Arrays.asList());
        responseDto.setPhones(Arrays.asList());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.phones").isArray())
                .andExpect(jsonPath("$.phones").isEmpty());
    }
    void createUserSuccessfully() throws Exception {

        UserRequestDto requestDto = createValidUserRequestDto();
        UserResponseDto responseDto = createUserResponseDto();

        when(userService.createUser(any(UserRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(responseDto.getId().toString()))
                .andExpect(jsonPath("$.name").value(responseDto.getName()))
                .andExpect(jsonPath("$.email").value(responseDto.getEmail()))
                .andExpect(jsonPath("$.token").value(responseDto.getToken()))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.modified").exists())
                .andExpect(jsonPath("$.last_login").exists())
                .andExpect(jsonPath("$.isactive").value(responseDto.getIsActive()))
                .andExpect(jsonPath("$.phones").isArray());
    }

    @Test
    void return409WhenEmailAlreadyExists() throws Exception {
        UserRequestDto requestDto = createValidUserRequestDto();
        
        when(userService.createUser(any(UserRequestDto.class)))
                .thenThrow(new EmailAlreadyExistsException("Email already exists"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Email already exists"));
    }

    @Test
    void return400WhenValidationErrors() throws Exception {
        UserRequestDto requestDto = createValidUserRequestDto();
        
        when(userService.createUser(any(UserRequestDto.class)))
                .thenThrow(new InvalidEmailFormatException("Invalid email format"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid email format"));

        when(userService.createUser(any(UserRequestDto.class)))
                .thenThrow(new InvalidPasswordFormatException("Password must contain at least one uppercase letter, one lowercase letter, and one number"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Password must contain at least one uppercase letter, one lowercase letter, and one number"));

        when(userService.createUser(any(UserRequestDto.class)))
                .thenThrow(new ValidationException("Name is required"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Name is required"));
    }

    @Test
    void return400WhenJsonIsMalformed() throws Exception {
        String malformedJson = "{ \"name\": \"Test User\", \"email\": \"test@gmail.com\" ";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid JSON format"));
    }

    @Test
    void return400WhenRequestBodyIsEmpty() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid JSON format"));
    }

}
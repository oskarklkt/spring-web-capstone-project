package org.example.springwebcapstoneproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.example.springwebcapstoneproject.dto.login.LoginRequestDto;
import org.example.springwebcapstoneproject.dto.register.RegisterUserDto;
import org.example.springwebcapstoneproject.exception.UserAlreadyExistsException;
import org.example.springwebcapstoneproject.exception.UserNotFoundException;
import org.example.springwebcapstoneproject.exception.WrongPasswordException;
import org.example.springwebcapstoneproject.service.implementation.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldReturnOkWhenUserIsRegistered() throws Exception {
        RegisterUserDto dto = new RegisterUserDto();
        dto.setEmail("test@example.com");
        dto.setPassword("secret");

        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(userService).register(any(RegisterUserDto.class));
    }

    @Test
    void shouldReturnConflictWhenUserAlreadyExists() throws Exception {
        RegisterUserDto dto = new RegisterUserDto();
        dto.setEmail("existing@example.com");
        dto.setPassword("secret");

        doThrow(new UserAlreadyExistsException(dto.getEmail()))
                .when(userService).register(any(RegisterUserDto.class));

        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value(
                        "User with email 'existing@example.com' already exists."));
    }

    @Test
    void shouldReturnOkWithSessionIdWhenLoginSuccessful() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("john@example.com", "pass");
        String sessionId = "SESSION123";

        when(userService.login(any(LoginRequestDto.class), any(HttpServletRequest.class))).thenReturn(sessionId);

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value(sessionId));
    }

    @Test
    void shouldReturnUnauthorizedWhenUserNotFound() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("missing@example.com", "pass");

        doThrow(new UserNotFoundException(dto.getEmail()))
                .when(userService).login(any(LoginRequestDto.class), any(HttpServletRequest.class));

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(
                        "User not found with email: missing@example.com"));
    }

    @Test
    void shouldReturnUnauthorizedWhenPasswordIsWrong() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("john@example.com", "wrongpass");

        doThrow(new WrongPasswordException())
                .when(userService).login(any(LoginRequestDto.class), any(HttpServletRequest.class));

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Wrong password."));
    }
}
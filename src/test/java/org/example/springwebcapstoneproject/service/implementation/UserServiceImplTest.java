package org.example.springwebcapstoneproject.service.implementation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.springwebcapstoneproject.dto.login.LoginRequestDto;
import org.example.springwebcapstoneproject.dto.register.RegisterUserDto;
import org.example.springwebcapstoneproject.entity.User;
import org.example.springwebcapstoneproject.exception.UserAlreadyExistsException;
import org.example.springwebcapstoneproject.exception.UserNotFoundException;
import org.example.springwebcapstoneproject.exception.WrongPasswordException;
import org.example.springwebcapstoneproject.mapper.UserMapper;
import org.example.springwebcapstoneproject.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        // given
        RegisterUserDto dto = new RegisterUserDto("john@example.com", "plainPassword");
        User mappedUser = new User(null, dto.getEmail(), dto.getPassword());

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userMapper.toEntity(dto)).thenReturn(mappedUser);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("hashedPassword");

        // when
        userService.register(dto);

        // then
        assertEquals("hashedPassword", mappedUser.getPassword());
        verify(userRepository).save(mappedUser);
    }

    @Test
    void shouldThrowExceptionWhenUserExists() {
        // given
        RegisterUserDto dto = new RegisterUserDto("existing@example.com", "secret");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        // then
        assertThrows(UserAlreadyExistsException.class, () -> userService.register(dto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldLoginSuccessfullyAndReturnSessionId() {
        // given
        String email = "user@example.com";
        String rawPassword = "password123";
        String hashedPassword = "hashedPassword";

        User user = new User(1L, email, hashedPassword);
        LoginRequestDto loginDto = new LoginRequestDto(email, rawPassword);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        when(userRepository.findUserByEmail(email)).thenReturn(java.util.Optional.of(user));
        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(true);
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn("mockSessionId");

        // when
        String sessionId = userService.login(loginDto, request);

        // then
        assertEquals("mockSessionId", sessionId);
        verify(session).setAttribute("userId", user.getId());
    }

    @Test
    void shouldThrowWhenUserNotFoundDuringLogin() {
        // given
        String email = "notfound@example.com";
        LoginRequestDto loginDto = new LoginRequestDto(email, "any");

        when(userRepository.findUserByEmail(email)).thenReturn(java.util.Optional.empty());

        // then
        assertThrows(UserNotFoundException.class, () -> userService.login(loginDto, mock(HttpServletRequest.class)));
    }

    @Test
    void shouldThrowWhenPasswordIsIncorrect() {
        // given
        String email = "user@example.com";
        String wrongPassword = "wrong";
        String hashedPassword = "correctHash";

        User user = new User(1L, email, hashedPassword);
        LoginRequestDto loginDto = new LoginRequestDto(email, wrongPassword);

        when(userRepository.findUserByEmail(email)).thenReturn(java.util.Optional.of(user));
        when(passwordEncoder.matches(wrongPassword, hashedPassword)).thenReturn(false);

        // then
        assertThrows(WrongPasswordException.class, () -> userService.login(loginDto, mock(HttpServletRequest.class)));
    }
}


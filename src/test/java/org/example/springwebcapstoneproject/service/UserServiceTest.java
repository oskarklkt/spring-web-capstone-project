package org.example.springwebcapstoneproject.service;

import org.example.springwebcapstoneproject.dto.RegisterUserDto;
import org.example.springwebcapstoneproject.entity.User;
import org.example.springwebcapstoneproject.exception.UserAlreadyExistsException;
import org.example.springwebcapstoneproject.mapper.UserMapper;
import org.example.springwebcapstoneproject.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

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
}

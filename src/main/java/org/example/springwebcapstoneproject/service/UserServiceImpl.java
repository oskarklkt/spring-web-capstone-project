package org.example.springwebcapstoneproject.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.springwebcapstoneproject.dto.login.LoginRequestDto;
import org.example.springwebcapstoneproject.dto.register.RegisterUserDto;
import org.example.springwebcapstoneproject.entity.User;
import org.example.springwebcapstoneproject.exception.UserAlreadyExistsException;
import org.example.springwebcapstoneproject.exception.UserNotFoundException;
import org.example.springwebcapstoneproject.exception.WrongPasswordException;
import org.example.springwebcapstoneproject.mapper.UserMapper;
import org.example.springwebcapstoneproject.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterUserDto registerUserDto) {
        throwIfUserExists(registerUserDto.getEmail());

        User user = userMapper.toEntity(registerUserDto);
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        userRepository.save(user);
    }

    public String login(LoginRequestDto loginRequestDto, HttpServletRequest request) {
        User user = getUserIfExists(loginRequestDto.getEmail());

        checkIfPasswordIsCorrect(user, loginRequestDto.getPassword());

        request.getSession().setAttribute("userId", user.getId());
        return request.getSession().getId();
    }

    private void throwIfUserExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException(email);
        }
    }

    private User getUserIfExists(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    private void checkIfPasswordIsCorrect(User user, String password) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new WrongPasswordException();
        }
    }
}

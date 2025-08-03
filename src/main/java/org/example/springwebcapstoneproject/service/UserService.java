package org.example.springwebcapstoneproject.service;

import lombok.RequiredArgsConstructor;
import org.example.springwebcapstoneproject.dto.RegisterUserDto;
import org.example.springwebcapstoneproject.entity.User;
import org.example.springwebcapstoneproject.exception.UserAlreadyExistsException;
import org.example.springwebcapstoneproject.mapper.UserMapper;
import org.example.springwebcapstoneproject.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterUserDto registerUserDto) {
        if (userRepository.existsByEmail(registerUserDto.getEmail())) {
            throw new UserAlreadyExistsException(registerUserDto.getEmail());
        }

        User user = userMapper.toEntity(registerUserDto);
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        userRepository.save(user);
    }
}

package org.example.springwebcapstoneproject.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.springwebcapstoneproject.dto.login.LoginRequestDto;
import org.example.springwebcapstoneproject.dto.register.RegisterUserDto;

public interface UserService {
    void register(RegisterUserDto registerUserDto);
    String login(LoginRequestDto loginRequestDto, HttpServletRequest request);
}

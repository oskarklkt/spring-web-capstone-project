package org.example.springwebcapstoneproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.springwebcapstoneproject.dto.login.LoginRequestDto;
import org.example.springwebcapstoneproject.dto.login.LoginResponseDto;
import org.example.springwebcapstoneproject.dto.register.RegisterUserDto;
import org.example.springwebcapstoneproject.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterUserDto registerUserDto) {
        userService.register(registerUserDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto,
                                                     HttpServletRequest request) {
        String sessionId = userService.login(loginRequestDto, request);
        return ResponseEntity.ok(new LoginResponseDto(sessionId));
    }
}

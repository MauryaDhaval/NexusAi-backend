package com.dhaval.nexusai.controller;

import com.dhaval.nexusai.dto.ApiResponseDto;
import com.dhaval.nexusai.dto.authDto.LoginRequestDto;
import com.dhaval.nexusai.dto.authDto.LoginResponseDto;
import com.dhaval.nexusai.dto.authDto.SignUpRequestDto;
import com.dhaval.nexusai.dto.authDto.SignUpResponseDto;
import com.dhaval.nexusai.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signUp")
    public ResponseEntity<ApiResponseDto<Void>> signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signUp(signUpRequestDto);
        return new ResponseEntity<>(ApiResponseDto.success("User Registered Successfully"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<LoginResponseDto>> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = authService.login(loginRequestDto);
        return new ResponseEntity<>(ApiResponseDto.success(loginResponseDto, "User Login Successfully"), HttpStatus.OK);
    }
}

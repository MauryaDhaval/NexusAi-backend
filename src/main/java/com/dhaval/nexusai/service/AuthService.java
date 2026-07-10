package com.dhaval.nexusai.service;

import com.dhaval.nexusai.dto.authDto.LoginRequestDto;
import com.dhaval.nexusai.dto.authDto.LoginResponseDto;
import com.dhaval.nexusai.dto.authDto.SignUpRequestDto;

public interface AuthService {

    void signUp (SignUpRequestDto signUpRequestDto);

    LoginResponseDto login (LoginRequestDto loginRequestDto);
}

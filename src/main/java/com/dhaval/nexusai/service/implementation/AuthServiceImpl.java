package com.dhaval.nexusai.service.implementation;

import com.dhaval.nexusai.dto.authDto.LoginRequestDto;
import com.dhaval.nexusai.dto.authDto.LoginResponseDto;
import com.dhaval.nexusai.dto.authDto.SignUpRequestDto;
import com.dhaval.nexusai.entity.User;
import com.dhaval.nexusai.entity.types.Plans;
import com.dhaval.nexusai.entity.types.Roles;
import com.dhaval.nexusai.error.customException.DuplicateResourceException;
import com.dhaval.nexusai.repository.UserRepository;
import com.dhaval.nexusai.security.JWTService;
import com.dhaval.nexusai.service.AuthService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Getter
@Setter
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Override
    public void signUp(SignUpRequestDto signUpRequestDto) {
        User existingUser = userRepository.findByEmail(signUpRequestDto.getEmail()).orElse(null);

        if(existingUser != null){
            throw new DuplicateResourceException("User already exists with this email");
        }

        User savedUser = userRepository.save(User.builder()
                .name(signUpRequestDto.getName())
                .email(signUpRequestDto.getEmail())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .role(Roles.USER)
                .plan(Plans.FREE)
                .build());

    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail() , loginRequestDto.getPassword())
        );

        String token =  jwtService.generateToken(loginRequestDto.getEmail());
        return LoginResponseDto
                .builder()
                .email(loginRequestDto.getEmail())
                .token(token)
                .build();
    }


}

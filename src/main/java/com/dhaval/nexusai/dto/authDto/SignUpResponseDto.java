package com.dhaval.nexusai.dto.authDto;

import lombok.Data;

@Data
public class SignUpResponseDto {
    private String name;
    private String email;
    private String password;
}

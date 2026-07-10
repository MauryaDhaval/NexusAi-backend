package com.dhaval.nexusai.dto.authDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {
    private String email;
    private String token;
}

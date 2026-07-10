package com.dhaval.nexusai.dto.chatDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class SendMessageResponseDto {
    private String message;
    private String response;
}

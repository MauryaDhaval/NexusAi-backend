package com.dhaval.nexusai.dto.chatDto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class SendMessageRequestDto {
    @NotBlank(message = "Message is required")
    private String message;
}

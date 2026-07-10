package com.dhaval.nexusai.dto.apiKeyDto;

import com.dhaval.nexusai.entity.types.ApiKeyStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GenerateApiKeyResponse {
    private String name;
    private String apiKey;
    private ApiKeyStatus status;
    private LocalDateTime createdAt;
}

package com.dhaval.nexusai.dto.apiKeyDto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetAllApiKeysResponseDto {
    private List<GenerateApiKeyResponse> generateApiKeyResponse;
}

package com.dhaval.nexusai.dto.apiKeyDto;

import com.dhaval.nexusai.entity.types.AiModels;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GenerateApiKeyRequest {
    @NotBlank(message = "Api Key Name is required")
    private String name;

    @NotNull(message = "Api Key model is required")
    private AiModels model;
}

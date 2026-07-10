package com.dhaval.nexusai.controller;

import com.dhaval.nexusai.dto.ApiResponseDto;
import com.dhaval.nexusai.dto.apiKeyDto.GenerateApiKeyRequest;
import com.dhaval.nexusai.dto.apiKeyDto.GenerateApiKeyResponse;
import com.dhaval.nexusai.dto.apiKeyDto.GetAllApiKeysResponseDto;
import com.dhaval.nexusai.dto.authDto.SignUpRequestDto;
import com.dhaval.nexusai.service.ApiKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api-keys")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @PostMapping
    public ResponseEntity<ApiResponseDto<GenerateApiKeyResponse>> generateApiKey(@Valid @RequestBody GenerateApiKeyRequest generateApiKeyRequest) {
        GenerateApiKeyResponse generateApiKeyResponse =  apiKeyService.generateApiKey(generateApiKeyRequest);
        return new ResponseEntity<>(ApiResponseDto.success(generateApiKeyResponse, "Api key generated successfully", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<GetAllApiKeysResponseDto>> getUserAllApiKey() {
        GetAllApiKeysResponseDto getAllApiKeysResponseDto =  apiKeyService.getUserApiKeys();
        return new ResponseEntity<>(ApiResponseDto.success(getAllApiKeysResponseDto, "Api keys fetched successfully"), HttpStatus.OK);
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<ApiResponseDto<Void>> deleteApiKey(@PathVariable String key) {
        apiKeyService.disableApiKey(key);
        return new ResponseEntity<>(ApiResponseDto.success("Api key disable successfully"), HttpStatus.OK);
    }
}

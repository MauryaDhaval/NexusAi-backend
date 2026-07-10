package com.dhaval.nexusai.service;

import com.dhaval.nexusai.dto.apiKeyDto.GenerateApiKeyRequest;
import com.dhaval.nexusai.dto.apiKeyDto.GenerateApiKeyResponse;
import com.dhaval.nexusai.dto.apiKeyDto.GetAllApiKeysResponseDto;
import com.dhaval.nexusai.entity.ApiKey;

public interface ApiKeyService {

    GenerateApiKeyResponse generateApiKey (GenerateApiKeyRequest generateApiKeyRequest);

    ApiKey findByKey (String key);

    void disableApiKey (String key);

    GetAllApiKeysResponseDto getUserApiKeys ();
}

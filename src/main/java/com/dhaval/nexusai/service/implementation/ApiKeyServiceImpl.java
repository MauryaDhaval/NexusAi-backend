package com.dhaval.nexusai.service.implementation;

import com.dhaval.nexusai.dto.apiKeyDto.GenerateApiKeyRequest;
import com.dhaval.nexusai.dto.apiKeyDto.GenerateApiKeyResponse;
import com.dhaval.nexusai.dto.apiKeyDto.GetAllApiKeysResponseDto;
import com.dhaval.nexusai.entity.ApiKey;
import com.dhaval.nexusai.entity.User;
import com.dhaval.nexusai.entity.types.ApiKeyStatus;
import com.dhaval.nexusai.error.customException.ResourceNotFoundException;
import com.dhaval.nexusai.repository.ApiKeyRepository;
import com.dhaval.nexusai.repository.UserRepository;
import com.dhaval.nexusai.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiKeyServiceImpl implements ApiKeyService {

    private final SecureRandom secureRandom = new SecureRandom();
    private final ApiKeyRepository apiKeyRepository;
    private final UserRepository userRepository;

    @Override
    public GenerateApiKeyResponse generateApiKey(GenerateApiKeyRequest generateApiKeyRequest) {

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new UsernameNotFoundException("User Not Found"));

        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        String randomPart = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        String apikey = "sk_live_" + randomPart;

        ApiKey saveApiKey =  apiKeyRepository.save(ApiKey.builder()
                .user(user)
                .key(apikey)
                .name(generateApiKeyRequest.getName())
                .model(generateApiKeyRequest.getModel())
                .status(ApiKeyStatus.ACTIVE)
                .build());


        return GenerateApiKeyResponse.builder()
                .name(saveApiKey.getName())
                .apiKey(saveApiKey.getKey())
                .status(saveApiKey.getStatus())
                .createdAt(saveApiKey.getCreatedAt())
                .build();
    }

    @Override
    public ApiKey findByKey(String key) {
            return apiKeyRepository
                    .findByKey(key)
                    .orElse(null);
    }

    @Override
    public void disableApiKey(String key) {
        ApiKey apiKey = apiKeyRepository.findByKey(key).orElseThrow(()-> new ResourceNotFoundException("Api Key Not Found"));
        apiKey.setStatus(ApiKeyStatus.REVOKE);
        apiKeyRepository.save(apiKey);
    }

    @Override
    public GetAllApiKeysResponseDto getUserApiKeys() {

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new UsernameNotFoundException("User Not Found"));

        List<ApiKey> apiKeys = apiKeyRepository.findByUser(user).orElse(new ArrayList<>());

        List<GenerateApiKeyResponse> generateApiKeyResponses = apiKeys.stream().map((
                apiKey ->
                        GenerateApiKeyResponse
                                .builder()
                                .name(apiKey.getName())
                                .apiKey(apiKey.getKey())
                                .status(apiKey.getStatus())
                                .createdAt(apiKey.getCreatedAt())
                                .build())).toList();


        return GetAllApiKeysResponseDto.builder().generateApiKeyResponse(generateApiKeyResponses).build();
    }

}

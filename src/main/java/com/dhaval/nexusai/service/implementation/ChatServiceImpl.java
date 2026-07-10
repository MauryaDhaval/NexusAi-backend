package com.dhaval.nexusai.service.implementation;

import com.dhaval.nexusai.dto.chatDto.SendMessageRequestDto;
import com.dhaval.nexusai.dto.chatDto.SendMessageResponseDto;
import com.dhaval.nexusai.entity.ApiKey;
import com.dhaval.nexusai.error.customException.RateLimitExceededException;
import com.dhaval.nexusai.repository.ApiKeyRepository;
import com.dhaval.nexusai.service.ApiKeyUsageService;
import com.dhaval.nexusai.service.ChatService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class ChatServiceImpl implements ChatService {

    private final OpenRouterService openRouterService;
    private final RateLimiterService rateLimiterService;
    private final ApiKeyUsageService apiKeyUsageService;
    private final ApiKeyRepository apiKeyRepository;

//    @Override
//    public SendMessageResponseDto processMessage(SendMessageRequestDto sendMessageRequestDto , String key) {
//
//        long start = System.currentTimeMillis();
//
//        if(!rateLimiterService.allowRequest(key)){
//
//            apiKeyUsageService.logUsage(
//                    key,
//                    "/chat/send-message",
//                    429,
//                    System.currentTimeMillis() - start
//                    );
//
//            throw new RateLimitExceededException(
//                    "Rate limit exceeded."
//            );
//        }
//
//        String aiResponse = openRouterService.askAi(sendMessageRequestDto.getMessage() , key);
//
//        apiKeyUsageService.logUsage(
//                key,
//                "/chat/send-message",
//                200,
//                System.currentTimeMillis() - start
//        );
//
//        return SendMessageResponseDto.builder().message(sendMessageRequestDto.getMessage()).response(aiResponse).build();
//    }

    @Override
    public SendMessageResponseDto processMessage(SendMessageRequestDto request, String key) {

        long start = System.currentTimeMillis();
        int status = 200;

        try {

            if (!rateLimiterService.allowRequest(key)) {
                status = 429;
                throw new RateLimitExceededException("Rate limit exceeded.");
            }

            ApiKey apiKeyConfig = apiKeyRepository.findByKey(key)
                    .orElseThrow(() -> new RuntimeException("Invalid API Key"));

            String modelSlug = apiKeyConfig.getModel().getModelId();

            String aiResponse = openRouterService.askAi(request.getMessage(), key , modelSlug);

            return SendMessageResponseDto.builder()
                    .message(request.getMessage())
                    .response(aiResponse)
                    .build();

        } catch (Exception e) {

            if (status == 200) {
                status = 500;
            }

            throw e;

        } finally {

            apiKeyUsageService.logUsage(
                    key,
                    "/chat/send-message",
                    status,
                    System.currentTimeMillis() - start
            );
        }
    }
}

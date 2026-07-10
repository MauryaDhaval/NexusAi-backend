package com.dhaval.nexusai.service.implementation;

import com.dhaval.nexusai.dto.dashboardDto.ApiKeyUsageResponseDto;
import com.dhaval.nexusai.dto.dashboardDto.DashboardStatsResponseDto;
import com.dhaval.nexusai.entity.ApiKey;
import com.dhaval.nexusai.entity.ApiKeyUsage;
import com.dhaval.nexusai.entity.User;
import com.dhaval.nexusai.error.customException.ResourceNotFoundException;
import com.dhaval.nexusai.repository.ApiKeyRepository;
import com.dhaval.nexusai.repository.ApiKeyUsageRepository;
import com.dhaval.nexusai.repository.UserRepository;
import com.dhaval.nexusai.service.ApiKeyUsageService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiKeyUsageServiceImpl implements ApiKeyUsageService {

    private final ApiKeyUsageRepository apiKeyUsageRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final UserRepository userRepository;

    @Override
    public Void logUsage(String key, String endpoints, Integer statusCode, Long responseTime) {

        ApiKey apiKey = apiKeyRepository.findByKey(key).orElseThrow(()-> new ResourceNotFoundException("Invalid key"));

        ApiKeyUsage apiKeyUsage = ApiKeyUsage
                .builder()
                .apiKey(apiKey)
                .endpoint(endpoints)
                .statusCode(statusCode)
                .responseTime(responseTime)
                .build();

        apiKeyUsageRepository.save(apiKeyUsage);

        return null;
    }

    @Override
    public DashboardStatsResponseDto getDashboardStats(String key) {

        ApiKey apiKey = apiKeyRepository.findByKey(key).orElseThrow(()->new ResourceNotFoundException("Invalid api key"));

        Long totalRequestByApi = apiKeyUsageRepository.countByApiKey(apiKey);
        Long totalSuccessRequest = apiKeyUsageRepository.countByApiKeyAndStatusCode(apiKey , 200);
        Long totalFailedRequest = totalRequestByApi - totalSuccessRequest;
        List<ApiKeyUsage> recentApiCalls = apiKeyUsageRepository.findTop10ByApiKeyOrderByCreatedAtDesc(apiKey);
        Double averageResponseTime = apiKeyUsageRepository.findAverageResponseTime(apiKey);

        List<ApiKeyUsageResponseDto> apiKeyUsageResponseDto = recentApiCalls.stream().map(
                (recentApiCall)-> ApiKeyUsageResponseDto
                        .builder()
                        .endpoint(recentApiCall.getEndpoint())
//                        .model(recentApiCall.getModel())
                        .statusCode(recentApiCall.getStatusCode())
                        .responseTime(recentApiCall.getResponseTime())
                        .build()).toList();

        DashboardStatsResponseDto dashboardStatsResponseDto = DashboardStatsResponseDto.builder()
                .totalRequest(totalRequestByApi)
                .successfulRequests(totalSuccessRequest)
                .failedRequests(totalFailedRequest)
                .apiKeyUsageResponseDtos(apiKeyUsageResponseDto)
                .averageResponseTime(averageResponseTime)
                .build();

        return dashboardStatsResponseDto;
    }

    @Override
    public DashboardStatsResponseDto getAggregateDashboardStats() {

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new UsernameNotFoundException("User Not Found"));

        // Gather metrics using the JPQL queries
        Long totalRequests = apiKeyUsageRepository.countByApiKey_User(user);
        Long successfulRequests = apiKeyUsageRepository.countByApiKey_UserAndStatusCode(user , 200);
        Long failedRequests = totalRequests - successfulRequests;
        Double averageResponseTime = apiKeyUsageRepository.findAverageResponseTimeByUser(user);

        if (averageResponseTime == null) {
            averageResponseTime = 0.0;
        }
        // Fetch recent 10 requests across all keys of the user
        List<ApiKeyUsage> recentCalls = apiKeyUsageRepository.findTop10ByApiKey_UserOrderByCreatedAtDesc(user);

        // Map logs to response DTOs including the active API key's model name
        List<ApiKeyUsageResponseDto> usageResponseDtos = recentCalls.stream().map(
                recentCall -> ApiKeyUsageResponseDto.builder()
                        .endpoint(recentCall.getEndpoint())
                        .model(recentCall.getApiKey().getModel().name()) // Traverses key to grab model name
                        .statusCode(recentCall.getStatusCode())
                        .responseTime(recentCall.getResponseTime())
                        .build()
        ).toList();

        return DashboardStatsResponseDto.builder()
                .totalRequest(totalRequests)
                .successfulRequests(successfulRequests)
                .failedRequests(failedRequests)
                .averageResponseTime(averageResponseTime)
                .apiKeyUsageResponseDtos(usageResponseDtos)
                .build();
    }
}

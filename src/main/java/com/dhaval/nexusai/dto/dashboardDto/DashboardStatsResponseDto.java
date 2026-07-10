package com.dhaval.nexusai.dto.dashboardDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class DashboardStatsResponseDto {

    private Long totalRequest;
    private Long successfulRequests;
    private Long failedRequests;
    private List<ApiKeyUsageResponseDto> apiKeyUsageResponseDtos;
    private Double averageResponseTime;

}

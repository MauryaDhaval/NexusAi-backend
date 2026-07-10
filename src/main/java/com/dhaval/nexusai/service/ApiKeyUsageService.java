package com.dhaval.nexusai.service;

import com.dhaval.nexusai.dto.dashboardDto.DashboardStatsResponseDto;
import com.dhaval.nexusai.entity.ApiKey;

public interface ApiKeyUsageService {

    Void logUsage (String key , String endpoints , Integer statusCode , Long responseTime);

    DashboardStatsResponseDto getDashboardStats (String key);

    DashboardStatsResponseDto getAggregateDashboardStats();
}

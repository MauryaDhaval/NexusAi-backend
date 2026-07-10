package com.dhaval.nexusai.dto.dashboardDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ApiKeyUsageResponseDto {
    private String endpoint;
    private String model;
    private Integer statusCode;
    private Long responseTime;
}

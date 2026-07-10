package com.dhaval.nexusai.controller;

import com.dhaval.nexusai.dto.ApiResponseDto;
import com.dhaval.nexusai.dto.authDto.SignUpRequestDto;
import com.dhaval.nexusai.dto.chatDto.SendMessageResponseDto;
import com.dhaval.nexusai.dto.dashboardDto.DashboardStatsResponseDto;
import com.dhaval.nexusai.service.ApiKeyUsageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashBoardController {

    private final ApiKeyUsageService apiKeyUsageService;

    @GetMapping("/apikey-stats/{key}")
    public ResponseEntity<ApiResponseDto<DashboardStatsResponseDto>> signUp(@PathVariable String key) {
        DashboardStatsResponseDto responseDto = apiKeyUsageService.getDashboardStats(key);
        return new ResponseEntity<>(ApiResponseDto.success(responseDto, "Stats fetch successfully"), HttpStatus.OK);
    }

    @GetMapping("/aggregate-stats")
    public ResponseEntity<ApiResponseDto<DashboardStatsResponseDto>> getAggregateStats() {
        DashboardStatsResponseDto responseDto = apiKeyUsageService.getAggregateDashboardStats();
        return new ResponseEntity<>(ApiResponseDto.success(responseDto, "Aggregate user statistics fetched successfully"), HttpStatus.OK);
    }
}

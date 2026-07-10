package com.dhaval.nexusai.dto;

import lombok.*;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ApiResponseDto <T> {
    private Boolean success;
    private String message;
    private int status;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    private T data;
    private String error = null;

    // Static helper for success response with data, message, and status
    public static <T> ApiResponseDto<T> success(T data, String message, HttpStatus status) {
        return ApiResponseDto.<T>builder()
                .success(true)
                .message(message)
                .status(status.value())
                .data(data)
                .build();
    }

    // Static helper for success response with data and message (defaults to 200 OK)
    public static <T> ApiResponseDto<T> success(T data, String message) {
        return success(data, message, HttpStatus.OK);
    }

    // Static helper for success response with message only
    public static <T> ApiResponseDto<T> success(String message) {
        return success(null, message, HttpStatus.OK);
    }

    // Static helper for error response with message, status, and error details
    public static <T> ApiResponseDto<T> error(String message, HttpStatus status, String errorDetails) {
        return ApiResponseDto.<T>builder()
                .success(false)
                .message(message)
                .status(status.value())
                .error(errorDetails)
                .build();
    }

    // Static helper for error response with message and status
    public static <T> ApiResponseDto<T> error(String message, HttpStatus status) {
        return error(message, status, null);
    }
}

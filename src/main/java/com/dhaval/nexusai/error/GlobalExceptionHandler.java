package com.dhaval.nexusai.error;

import com.dhaval.nexusai.dto.ApiResponseDto;
import com.dhaval.nexusai.error.customException.DuplicateResourceException;
import com.dhaval.nexusai.error.customException.InvalidApiKeyException;
import com.dhaval.nexusai.error.customException.RateLimitExceededException;
import com.dhaval.nexusai.error.customException.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<?>> generalExceptionHandler (Exception ex){
        return buildErrorResponse(HttpStatus.NOT_FOUND , ex.getLocalizedMessage(), ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDto<?>> resourceNotFoundExceptionHandler (ResourceNotFoundException ex){
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR , ex.getLocalizedMessage(), ex.getMessage());
    }

    @ExceptionHandler(InvalidApiKeyException.class)
    public ResponseEntity<ApiResponseDto<?>> invalidApiKeyExceptionHandler (InvalidApiKeyException ex){
        return buildErrorResponse(HttpStatus.UNAUTHORIZED , ex.getLocalizedMessage(), ex.getMessage());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponseDto<?>> duplicateResourcesExceptionHandler(DuplicateResourceException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getLocalizedMessage(), ex.getMessage());
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ApiResponseDto<?>> rateLimitExceededExceptionHandler(RateLimitExceededException ex) {
        return buildErrorResponse(HttpStatus.TOO_MANY_REQUESTS, ex.getLocalizedMessage(), ex.getMessage());
    }



    private ResponseEntity<ApiResponseDto<?>> buildErrorResponse (HttpStatus status ,String localMessage , String error) {
        return new ResponseEntity<>(ApiResponseDto.error(localMessage, status, error), status);
    }

}

package com.dhaval.nexusai.controller;

import com.dhaval.nexusai.dto.ApiResponseDto;
import com.dhaval.nexusai.dto.authDto.LoginResponseDto;
import com.dhaval.nexusai.dto.chatDto.SendMessageRequestDto;
import com.dhaval.nexusai.dto.chatDto.SendMessageResponseDto;
import com.dhaval.nexusai.entity.ApiKey;
import com.dhaval.nexusai.service.ChatService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.time.LocalDateTime;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/send-message")
    public ResponseEntity<ApiResponseDto<SendMessageResponseDto>> processMessage (@Valid @RequestBody SendMessageRequestDto sendMessageRequestDto , HttpServletRequest request){
        ApiKey apiKey = (ApiKey) request.getAttribute("apiKey");
        SendMessageResponseDto sendMessageResponseDto = chatService.processMessage(sendMessageRequestDto , apiKey.getKey());
        return new ResponseEntity<>(ApiResponseDto.success(sendMessageResponseDto, "Message Process Successfully"), HttpStatus.OK);
    }
}

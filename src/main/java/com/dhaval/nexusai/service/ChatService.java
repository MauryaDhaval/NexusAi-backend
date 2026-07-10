package com.dhaval.nexusai.service;

import com.dhaval.nexusai.dto.chatDto.SendMessageRequestDto;
import com.dhaval.nexusai.dto.chatDto.SendMessageResponseDto;

public interface ChatService {
    SendMessageResponseDto processMessage (SendMessageRequestDto sendMessageRequestDto , String key);
}

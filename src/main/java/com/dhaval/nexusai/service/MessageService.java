package com.dhaval.nexusai.service;

import com.dhaval.nexusai.dto.openRouter.OpenRouterMessage;
import com.dhaval.nexusai.entity.types.MessageRoles;

import java.util.List;

public interface MessageService {
    void saveMessage(MessageRoles role , String content , String key);
    List<OpenRouterMessage> getRecentsMessages (String key);
}

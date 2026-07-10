package com.dhaval.nexusai.service.implementation;

import com.dhaval.nexusai.dto.openRouter.OpenRouterMessage;
import com.dhaval.nexusai.entity.ApiKey;
import com.dhaval.nexusai.entity.Message;
import com.dhaval.nexusai.entity.types.MessageRoles;
import com.dhaval.nexusai.error.customException.InvalidApiKeyException;
import com.dhaval.nexusai.error.customException.ResourceNotFoundException;
import com.dhaval.nexusai.repository.ApiKeyRepository;
import com.dhaval.nexusai.repository.MessageRepository;
import com.dhaval.nexusai.service.MessageService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ApiKeyRepository apiKeyRepository;

    @Override
    public void saveMessage(MessageRoles role , String content , String key) {

        ApiKey apiKey = apiKeyRepository.findByKey(key).orElseThrow(()-> new InvalidApiKeyException("Invalid Api Key"));

        Message message = Message.builder().apiKey(apiKey).role(role).content(content).build();

        messageRepository.save(message);
    }

    public List<OpenRouterMessage> getRecentsMessages (String key) {

        ApiKey apiKey = apiKeyRepository.findByKey(key).orElseThrow(()-> new InvalidApiKeyException("Invalid Api Key"));
        List<Message> messages = messageRepository.findTop20ByApiKeyOrderByCreatedAtDesc(apiKey);


        return messages.stream().map(
                message -> OpenRouterMessage.builder().role(message.getRole().name()).content(message.getContent()).build()
        ).toList();
    }


}

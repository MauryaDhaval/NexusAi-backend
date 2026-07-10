package com.dhaval.nexusai.service.implementation;

import com.dhaval.nexusai.config.OpenRouterConfig;
import com.dhaval.nexusai.dto.openRouter.OpenRouterMessage;
import com.dhaval.nexusai.dto.openRouter.OpenRouterRequestDto;
import com.dhaval.nexusai.dto.openRouter.OpenRouterResponse;
import com.dhaval.nexusai.entity.types.MessageRoles;
import com.dhaval.nexusai.service.MessageService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenRouterService {

    private final RestClient restClient;
    private final OpenRouterConfig openRouterConfig;
    private final MessageService messageService;

    public String askAi (String prompt , String key , String modelSlug) {

        messageService.saveMessage(MessageRoles.user,prompt,key);


        // Fetch recent records (comes out unmodifiable from your data layer)
        List<OpenRouterMessage> rawMessages = messageService.getRecentsMessages(key);

        List<OpenRouterMessage> messages = new java.util.ArrayList<>(rawMessages);

        Collections.reverse(messages);
        OpenRouterRequestDto requestDto = OpenRouterRequestDto.builder().model(modelSlug).messages(messages).build();
        OpenRouterResponse response = restClient.post()
                .uri( openRouterConfig.getApiUrl() + "/chat/completions")
                .header(HttpHeaders.AUTHORIZATION,"Bearer " + openRouterConfig.getApiKey())
                .header(HttpHeaders.CONTENT_TYPE , MediaType.APPLICATION_JSON_VALUE)
                .body(requestDto)
                .retrieve()
                .body(OpenRouterResponse.class);

        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            throw new RuntimeException("OpenRouter API returned an empty or invalid response. Please verify model slug or api key.");
        }
        messageService.saveMessage(MessageRoles.assistant , response.getChoices().get(0).getMessage().getContent() , key);


        return response.getChoices()
                .get(0)
                .getMessage()
                .getContent();
    }
}

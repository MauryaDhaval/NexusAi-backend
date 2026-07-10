package com.dhaval.nexusai.dto.openRouter;

import lombok.*;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class OpenRouterRequestDto {
    private String model;
    private List<OpenRouterMessage> messages;
}

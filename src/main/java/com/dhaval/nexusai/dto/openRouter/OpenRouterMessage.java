package com.dhaval.nexusai.dto.openRouter;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class OpenRouterMessage {
    private String role;
    private String content;
}

package com.dhaval.nexusai.dto.openRouter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Choice {
    private OpenRouterMessage message;
}

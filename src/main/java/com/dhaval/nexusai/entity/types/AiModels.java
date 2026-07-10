package com.dhaval.nexusai.entity.types;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum AiModels {
    NVIDIA_NANO("nvidia/nemotron-3-nano-30b-a3b:free"),
    NVIDIA_OMNI_REASONING("nvidia/nemotron-3-nano-omni-30b-a3b-reasoning:free"),
    COHERE_MINI_CODE("cohere/north-mini-code:free");

    private final String modelId;
}

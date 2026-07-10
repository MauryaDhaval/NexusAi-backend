package com.dhaval.nexusai.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "openrouter")
@Getter
@Setter
public class OpenRouterConfig {
    private String apiUrl;
    private String apiKey;
//    private String model;
}

package com.dhaval.nexusai;

import com.dhaval.nexusai.config.OpenRouterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@ConfigurationPropertiesScan
public class NexusaiApplication {

	public static void main(String[] args) {
		SpringApplication.run(NexusaiApplication.class, args);
	}

}

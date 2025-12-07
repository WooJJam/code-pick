package com.codepick.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient solvedAcWebClient() {
        return WebClient.builder()
                .baseUrl("https://solved.ac/api/v3")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}

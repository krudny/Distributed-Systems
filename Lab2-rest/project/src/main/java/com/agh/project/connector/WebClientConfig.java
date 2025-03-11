package com.agh.project.connector;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${google.api.key}")
    private String API_KEY;

    @Bean
    public WebClient punkApiWebClient(WebClient.Builder builder) {
        return builder.baseUrl("https://punkapi.online/v3").build();
    }

    @Bean
    public WebClient translatorApiWebClient(WebClient.Builder builder) {
        return builder.baseUrl("https://translation.googleapis.com/language/translate/v2?key=AIzaSyD7w6B4s9Vqp7V3_uijp-ZKObqv4GPgDTo").build();
    }
}

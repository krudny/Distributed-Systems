package com.agh.project.service;

import com.agh.project.connector.ExternalAPI;
import com.agh.project.model.Language;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class LanguageService {
    private final Language language;
    private final ExternalAPI externalAPI;

    public HashMap<String, String> getLanguages() {
        return language.getLanguages();
    }

    public Mono<String> translate(String text, String language) {
        return externalAPI.translate(text, language);
    }
}

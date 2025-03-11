package com.agh.project.configurator;

import com.agh.project.model.Language;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@AllArgsConstructor
public class LanguageConfigurator {
    private Language language;

    @PostConstruct
    public void createLanguages() {
        HashMap<String, String> languages = new HashMap<>();
        languages.put("English", "en");
        languages.put("Polski", "pl");
        languages.put("Français", "fr");
        languages.put("Deutsch", "de");
        languages.put("Español", "es");
        languages.put("Italiano", "it");

        language.setLanguages(languages);
    }
}
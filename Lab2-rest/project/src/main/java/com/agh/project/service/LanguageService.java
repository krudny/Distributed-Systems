package com.agh.project.service;

import com.agh.project.model.Language;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LanguageService {
    private final Language language;

    public List<String> getLanguages() {
        return language.getLanguages().keySet().stream().toList();
    }
}

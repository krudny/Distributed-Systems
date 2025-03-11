package com.agh.project.controller;

import com.agh.project.service.LanguageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/languages")
@AllArgsConstructor
public class LanguageController {
    private final LanguageService languageService;

    @GetMapping()
    public List<String> getLanguages() {
        return languageService.getLanguages();
    }
}

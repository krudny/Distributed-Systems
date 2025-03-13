package com.agh.project.controller;

import com.agh.project.service.LanguageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/languages")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class LanguageController {
    private final LanguageService languageService;

    @GetMapping()
    public HashMap<String, String> getLanguages() {
        return languageService.getLanguages();
    }

    @PostMapping("/translate")
    public Mono<String> translate(@RequestBody Map<String, String> body) {
        String text = body.get("text");
        String language = body.get("language");
        return languageService.translate(text, language);
    }
}

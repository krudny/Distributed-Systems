package com.agh.project.service;

import com.agh.project.connector.ExternalAPI;
import com.agh.project.model.Beer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BeerService {
    private final ExternalAPI externalAPI;
    private final LanguageService languageService;

    public Flux<Beer> getBeers(int page, int perPage, String language) {
        return externalAPI.fetchBeers(page, perPage)
                .flatMap(beer -> translateBeer(beer, language));
    }

    public Flux<Beer> getRandomBeers(int quantity, String language) {
        return Flux.range(0, quantity)
                .flatMap(i -> externalAPI.fetchRandomBeers()
                        .flatMap(beer -> translateBeer(beer, language)));
    }

    private Mono<Beer> translateBeer(Beer beer, String language) {
        return Mono.zip(
                languageService.translate(beer.getDescription(), language),
                languageService.translate(beer.getTagline(), language),
                (description, tagline) -> {
                    beer.setDescription(description);
                    beer.setTagline(tagline);
                    return beer;
                }
        );
    }


}

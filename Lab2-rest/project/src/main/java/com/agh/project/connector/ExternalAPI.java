package com.agh.project.connector;

import com.agh.project.model.Beer;
import com.jayway.jsonpath.JsonPath;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@AllArgsConstructor
public class ExternalAPI {
    private final WebClient punkApiWebClient;
    private final WebClient translatorApiWebClient;

    public Flux<Beer> fetchRandomBeers() {
        return punkApiWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/beers/random")
                        .build())
                .retrieve()
                .bodyToFlux(Beer.class);
    }

    public Flux<Beer> fetchBeers(int page, int perPage) {
        return punkApiWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/beers")
                        .queryParam("page", page)
                        .queryParam("per_page", perPage)
                        .build())
                .retrieve()
                .bodyToFlux(Beer.class);
    }

    public Mono<String> translate(String text, String targetLanguage) {
        return translatorApiWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("target", targetLanguage)
                        .queryParam("q", text)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> JsonPath.read(response, "$.data.translations[0].translatedText"));
    }

}

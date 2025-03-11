package com.agh.project.connector;

import com.agh.project.model.Beer;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service

public class ExternalAPI {
    private final WebClient webClient;

    @Autowired
    public ExternalAPI(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<Beer> fetchBeers(int page, int perPage) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/beers")
                        .queryParam("page", page)
                        .queryParam("per_page", perPage)
                        .build())
                .retrieve()
                .bodyToFlux(Beer.class);
    }

}

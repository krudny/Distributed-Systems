package com.agh.project.controller;

import com.agh.project.model.Beer;
import com.agh.project.service.BeerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/beers")
@AllArgsConstructor
public class BeerController {
    private final BeerService beerService;

    @GetMapping
    public Mono<List<Beer>> getBeers(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int perPage,
                                     @RequestParam(defaultValue = "pl") String language) {
        return beerService.getBeers(page, perPage, language).collectList();
    }

    @GetMapping("/random")
    public Mono<List<Beer>> getBeers(@RequestParam(defaultValue = "5") int quantity,
                                     @RequestParam(defaultValue = "pl") String language) {
        return beerService.getRandomBeers(quantity, language).collectList();
    }
}

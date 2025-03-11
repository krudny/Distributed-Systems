package com.agh.project.controller;

import com.agh.project.model.Beer;
import com.agh.project.service.BeerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/beers")
@AllArgsConstructor
public class BeerController {
    private final BeerService beerService;

    @GetMapping
    public List<Beer> getBeers(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int perPage) {
        return beerService.getBeers(page, perPage);
    }
}

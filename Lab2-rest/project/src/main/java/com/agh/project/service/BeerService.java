package com.agh.project.service;

import com.agh.project.connector.ExternalAPI;
import com.agh.project.model.Beer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BeerService {
    private final ExternalAPI externalAPI;

    public List<Beer> getBeers(int page, int perPage) {
        return externalAPI.fetchBeers(page, perPage)
                .collectList()
                .block();
    }
}

package com.agh.project.service;

import com.agh.project.connector.ExternalAPI;
import com.agh.project.model.Beer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeerService {
    private final ExternalAPI externalAPI;

    public BeerService(ExternalAPI externalAPI) {
        this.externalAPI = externalAPI;
    }

    public List<Beer> getBeers(int page, int perPage) {
        return externalAPI.fetchBeers(page, perPage)
                .collectList()
                .block();
    }
}

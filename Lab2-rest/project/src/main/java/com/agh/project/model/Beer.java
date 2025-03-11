package com.agh.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Beer {
    @JsonProperty("name")
    private String name;

    @JsonProperty("tagline")
    private String tagline;

    @JsonProperty("abv")
    private Double abv;

    @JsonProperty("description")
    private String description;

    @JsonProperty("image")
    private String image;

    @Override
    public String toString() {
        return "Beer{name='" + name + "', tagline='" + tagline + "', abv=" + abv + "}";
    }
}

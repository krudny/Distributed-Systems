package com.agh.project.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Getter
@Setter
@Component
public class Language {
    private HashMap<String, String> languages;
}

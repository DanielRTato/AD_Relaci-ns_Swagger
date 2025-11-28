package org.example.controller;

import org.example.repository.TitorRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/titores")
public class TitorController {

    private final TitorRepository titorRepository;

    public TitorController(TitorRepository titorRepository) {
        this.titorRepository = titorRepository;
    }
}

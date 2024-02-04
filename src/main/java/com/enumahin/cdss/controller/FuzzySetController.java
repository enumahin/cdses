package com.enumahin.cdss.controller;

import com.enumahin.cdss.model.FuzzySet;
import com.enumahin.cdss.service.FuzzySetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("fuzzy_sets")
public class FuzzySetController {

    private final FuzzySetService fuzzySetService;

    public FuzzySetController(FuzzySetService fuzzySetService) {
        this.fuzzySetService = fuzzySetService;
    }

    @GetMapping
    public List<FuzzySet> getSets(){
        return fuzzySetService.findAll();
    }

    @PostMapping(value = "/add")
    public ResponseEntity<FuzzySet> create(@RequestBody FuzzySet fuzzySet) {
        System.out.println(fuzzySet);
        return new ResponseEntity<>(fuzzySetService.addSet(fuzzySet), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<FuzzySet> create(@PathVariable("id") Integer fuzzySetId) {
        return fuzzySetService.getSet(fuzzySetId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}

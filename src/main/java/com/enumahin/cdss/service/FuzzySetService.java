package com.enumahin.cdss.service;

import com.enumahin.cdss.model.FuzzySet;
import com.enumahin.cdss.repository.FuzzySetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuzzySetService {

    private final FuzzySetRepository fuzzySetRepository;

    public FuzzySetService(FuzzySetRepository fuzzySetRepository) {
        this.fuzzySetRepository = fuzzySetRepository;
    }

    public FuzzySet addSet(FuzzySet fuzzySet){
        return fuzzySetRepository.save(fuzzySet);
    }
    public Optional<FuzzySet> getSet(Integer fuzzySetId){
        return fuzzySetRepository.findById(fuzzySetId);
    }

    public List<FuzzySet> findAll(){
        return fuzzySetRepository.findAll();
    }

    public void deleteSet(Integer setId){
         fuzzySetRepository.deleteById(setId);
    }
}

package com.enumahin.cdss.service;

import com.enumahin.cdss.model.Degree;
import com.enumahin.cdss.repository.DegreeRepository;
import org.springframework.stereotype.Service;

@Service
public class DegreeService {

    private final DegreeRepository degreeRepository;

    public DegreeService(DegreeRepository degreeRepository) {
        this.degreeRepository = degreeRepository;
    }
}

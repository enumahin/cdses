package com.enumahin.cdss.repository;

import com.enumahin.cdss.model.FuzzySet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuzzySetRepository extends CrudRepository<FuzzySet, Integer> {
}

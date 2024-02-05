package com.enumahin.cdss.repository;

import com.enumahin.cdss.model.FuzzySet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface FuzzySetRepository extends CrudRepository<FuzzySet, Integer> {

    List<FuzzySet> findAll();

    FuzzySet save(FuzzySet fuzzySet);
}

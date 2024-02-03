package com.enumahin.cdss.repository;

import com.enumahin.cdss.model.DegreeLevel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DegreeLevelRepository extends CrudRepository<DegreeLevel, Integer> {
}

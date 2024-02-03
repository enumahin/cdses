package com.enumahin.cdss.repository;

import com.enumahin.cdss.model.Degree;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DegreeRepository extends CrudRepository<Degree, Integer> {
}

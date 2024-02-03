package com.enumahin.cdss.repository;

import com.enumahin.cdss.model.Degree;
import com.enumahin.cdss.model.MemberDegree;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberDegreeRepository extends CrudRepository<MemberDegree, Integer> {
}

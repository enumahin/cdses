package com.enumahin.cdss.repository;

import com.enumahin.cdss.model.Membership;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipRepository extends CrudRepository<Membership, Integer> {
}

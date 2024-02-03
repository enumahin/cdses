package com.enumahin.cdss.repository;

import com.enumahin.cdss.model.SetMember;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SetMemberRepository extends CrudRepository<SetMember, Integer> {
}

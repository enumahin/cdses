package com.enumahin.cdss.repository;

import com.enumahin.cdss.model.Degree;
import com.enumahin.cdss.model.MemberDegree;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberDegreeRepository extends CrudRepository<MemberDegree, Integer> {

    List<MemberDegree> findAll();

    MemberDegree save(MemberDegree memberDegree);

    @Query("Select m from MemberDegree as m where m.memberId=?1")
    List<MemberDegree> findAllByMemberId(Integer memberId);

    @Query("Select m from MemberDegree as m where m.memberId=?1 AND m.degree=?2")
    Optional<MemberDegree> findAllByMemberIdAndDegree(Integer memberId, Degree degree);
}

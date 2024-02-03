package com.enumahin.cdss.repository;

import com.enumahin.cdss.model.Degree;
import com.enumahin.cdss.model.Equality;
import com.enumahin.cdss.model.Membership;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembershipRepository extends CrudRepository<Membership, Integer> {

    List<Membership> findAll();

    Membership save(Membership membership);

    @Query("Select m from Membership as m where m.member.memberId=?1 AND m.degreeOfMembership=?2")
    List<Membership> equalTo(Integer memberId, Double degree);

    @Query("Select m from Membership as m where m.member.memberId=?1 AND m.degreeOfMembership<=?2")
    List<Membership> lessThanOrEqualTo(Integer memberId, Double degree);

    @Query("Select m from Membership as m where m.member.memberId=?1 AND m.degreeOfMembership<?2")
    List<Membership> lessThan(Integer memberId, Double degree);

    @Query("Select m from Membership as m where m.member.memberId=?1 AND m.degreeOfMembership>?2")
    List<Membership> greaterThan(Integer memberId, Double degree);

    @Query("Select m from Membership as m where m.member.memberId=?1 AND m.degreeOfMembership>=?2")
    List<Membership> greaterThanOrEqualTo(Integer memberId, Double degree);

    @Query("Select m from Membership as m where m.set.setId=?1")
    List<Membership> findAllByFuzzySetId(Integer id);

    @Query("Select m from Membership as m where m.member.memberId=?1")
    List<Membership> findAllByMemberId(Integer id);
}

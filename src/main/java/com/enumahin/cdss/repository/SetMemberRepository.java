package com.enumahin.cdss.repository;

import com.enumahin.cdss.model.SetMember;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface SetMemberRepository extends CrudRepository<SetMember, Integer> {

    List<SetMember> findAll();

    SetMember save(SetMember setMember);
}

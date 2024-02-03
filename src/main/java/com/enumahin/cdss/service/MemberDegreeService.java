package com.enumahin.cdss.service;

import com.enumahin.cdss.model.Degree;
import com.enumahin.cdss.model.MemberDegree;
import com.enumahin.cdss.repository.MemberDegreeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberDegreeService {

    private final MemberDegreeRepository memberDegreeRepository;

    public MemberDegreeService(MemberDegreeRepository memberDegreeRepository) {
        this.memberDegreeRepository = memberDegreeRepository;
    }

    public MemberDegree addMemberDegree(MemberDegree memberDegree){
        return memberDegreeRepository.save(memberDegree);
    }

    public Optional<MemberDegree> getById(Integer id){
        return memberDegreeRepository.findById(id);
    }

    public List<MemberDegree> findAll(){
        return memberDegreeRepository.findAll();
    }

    public List<MemberDegree> getByMemberId(Integer memberId){
        return memberDegreeRepository.findAllByMemberId(memberId);
    }

    public Optional<MemberDegree> getByMemberIdAndDegree(Integer memberId, Degree degree){
        return memberDegreeRepository.findAllByMemberIdAndDegree(memberId, degree);
    }
}

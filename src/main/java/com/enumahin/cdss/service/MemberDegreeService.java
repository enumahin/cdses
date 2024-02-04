package com.enumahin.cdss.service;

import com.enumahin.cdss.model.Degree;
import com.enumahin.cdss.model.MemberDegree;
import com.enumahin.cdss.model.SetMember;
import com.enumahin.cdss.model.dto.MemberDegreeDto;
import com.enumahin.cdss.model.dto.MemberDegreeResponse;
import com.enumahin.cdss.repository.MemberDegreeRepository;
import com.enumahin.cdss.repository.SetMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class MemberDegreeService {

    @Autowired
    private SetMemberRepository setMemberRepository;
    private final MemberDegreeRepository memberDegreeRepository;

    public MemberDegreeService(MemberDegreeRepository memberDegreeRepository) {
        this.memberDegreeRepository = memberDegreeRepository;
    }

    public MemberDegree addMemberDegree(MemberDegreeDto memberDegree){
        return setMemberRepository.findById(memberDegree.getMemberId()).map(setMember ->
                     memberDegreeRepository.save(
                            MemberDegree.builder()
                                    .memberId(setMember)
                                    .degree(memberDegree.getDegree())
                                    .description(memberDegree.getDescription())
                                    .build()
            ) ).orElse(null);

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

    public List<MemberDegreeResponse> memberDegreeResponse(){
        return findAll().stream().map(build()).toList();
    }

    public Function<MemberDegree, MemberDegreeResponse> build(){
        return memberDegree -> MemberDegreeResponse.builder()
                .memberDegreeId(memberDegree.getMemberDegreeId())
                .memberId(memberDegree.getMemberId().getMemberId())
                .memberName(memberDegree.getMemberId().getMemberName())
                .degreeOfMembership(memberDegree.getDegree())
                .degreeDescription(memberDegree.getDescription())
                .build();
    }

    public String getMembershipDescription(Integer memberId, Double degree){
        return getByMemberIdAndDegree(memberId, degree).map(MemberDegree::getDescription).orElse("");
    }

    public Optional<MemberDegree> getByMemberIdAndDegree(Integer memberId, Double degree){
        return memberDegreeRepository.findAllByMemberIdAndDegree(memberId, degree);
    }
}

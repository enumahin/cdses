package com.enumahin.cdss.service;

import com.enumahin.cdss.model.Degree;
import com.enumahin.cdss.model.Equality;
import com.enumahin.cdss.model.FuzzySet;
import com.enumahin.cdss.model.Membership;
import com.enumahin.cdss.model.dto.MatchList;
import com.enumahin.cdss.model.dto.MatchResponse;
import com.enumahin.cdss.model.dto.MembershipDto;
import com.enumahin.cdss.model.dto.MembershipResponse;
import com.enumahin.cdss.repository.FuzzySetRepository;
import com.enumahin.cdss.repository.MembershipRepository;
import com.enumahin.cdss.repository.SetMemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MembershipService {

    private final MembershipRepository membershipRepository;

    private final MemberDegreeService memberDegreeService;
    @Autowired
    private SetMemberRepository setMemberRepository;
    @Autowired
    private FuzzySetRepository fuzzySetRepository;

    // Map<SetId, Map<TotalMemberCount, RequiredMemberCount>
    private final Map<Integer, MatchResponse> setSignatureMap = new HashMap<>();

    public MembershipService(MembershipRepository membershipRepository, MemberDegreeService memberDegreeService) {
        this.membershipRepository = membershipRepository;
        this.memberDegreeService = memberDegreeService;
    }

    @Transactional
    public Membership addMembership(MembershipDto membershipDto){
       return fuzzySetRepository.findById(membershipDto.getFuzzySetId())
               .flatMap(fuzzySet -> setMemberRepository.findById(membershipDto.getMemberId())
               .map(setMember -> membershipRepository.save(
                       Membership.builder()
                               .set(fuzzySet)
                               .member(setMember)
                               .degreeOfMembership(membershipDto.getDegree())
                               .required(membershipDto.getRequired())
                               .build()
               ))).orElse(null);

    }

    public List<Membership> getMembership(Integer memberId, Degree degree, Equality equality){
        if(equality.equals(Equality.EQ))
            return getMemberShipEqualTo(memberId, degree);
        if(equality.equals(Equality.GT))
            return getMemberShipGreaterThan(memberId, degree);
        if(equality.equals(Equality.GTE))
            return getMemberShipGreaterThanOrEqualTo(memberId, degree);
        if(equality.equals(Equality.LT))
            return getMemberShipLessThan(memberId, degree);
        if(equality.equals(Equality.LTE))
            return getMemberShipLessThanOrEqualTo(memberId, degree);
        return null;
    }

    private List<Membership> getMemberShipEqualTo(Integer memberId, Degree degree){
        return membershipRepository.equalTo(memberId, degree.label);
    }

    private List<Membership> getMemberShipLessThanOrEqualTo(Integer memberId, Degree degree){
        return membershipRepository.lessThanOrEqualTo(memberId, degree.label);
    }

    private List<Membership> getMemberShipLessThan(Integer memberId, Degree degree){
        return membershipRepository.lessThan(memberId, degree.label);
    }

    private List<Membership> getMemberShipGreaterThan(Integer memberId, Degree degree){
        return membershipRepository.greaterThan(memberId, degree.label);
    }

    private List<Membership> getMemberShipGreaterThanOrEqualTo(Integer memberId, Degree degree){
        return membershipRepository.greaterThanOrEqualTo(memberId, degree.label);
    }

    public List<Membership> findAll() {
        return membershipRepository.findAll();
    }

    public List<MembershipResponse> build(){
        return findAll().stream()
                .map(getMembershipMembershipResponseFunction()
                ).toList();
    }

    private Function<Membership, MembershipResponse> getMembershipMembershipResponseFunction() {
        return membership -> MembershipResponse
                .builder()
                .membershipId(membership.getMembershipId())
                .setId(membership.getSet().getSetId())
                .setName(membership.getSet().getSetName())
                .memberId(membership.getMember().getMemberId())
                .memberName(membership.getMember().getMemberName())
                .degreeOfMembership(membership.getDegreeOfMembership())
                .degreeDescription(memberDegreeService.getMembershipDescription(membership.getMember().getMemberId(),
                        membership.getDegreeOfMembership()))
                .required(membership.getRequired())
                .build();
    }

    public Optional<Membership> getById(Integer id) {
        return membershipRepository.findById(id);
    }

    public List<Membership> getBySetId(Integer id) {
        return membershipRepository.findAllByFuzzySetId(id);
    }

    public List<Membership> getByMemberId(Integer id) {
        return membershipRepository.findAllByMemberId(id);
    }

    public Map<FuzzySet, Map<Boolean, Long>> match(MatchList matchList) {
       return matchList.getMatchList().stream()
               .map(match -> membershipRepository.equalTo(match.getMemberId(), match.getDegree()))
               .flatMap(List::stream)
//               .map(set -> set.getSet().getSetName())
               .collect(Collectors.groupingBy(Membership::getSet, Collectors.groupingBy(Membership::getRequired, Collectors.counting())));
    }

    public List<MatchResponse> matchStructure(MatchList matchList){
        Map<FuzzySet, Map<Boolean, Long>> match = match(matchList);
        System.out.println(match);
        return match.entrySet().stream()
                .map(this::fromMembership).toList();

    }

    private MatchResponse fromMembership(Map.Entry<FuzzySet, Map<Boolean, Long>> membershipMap){
         return MatchResponse.builder()
                .setId(membershipMap.getKey().getSetId())
                .setName(membershipMap.getKey().getSetName())
                .actualRequiredCount(
                        membershipMap.getValue().get(true) != null ? membershipMap.getValue().get(true).intValue() :0)
                .expectedRequiredCount(mapSignature(membershipMap.getKey().getSetId()).getExpectedRequiredCount())
                .actualTotalCount(
                        (membershipMap.getValue().get(false) != null ? membershipMap.getValue().get(false).intValue() : 0) +
                                (membershipMap.getValue().get(true) != null ? membershipMap.getValue().get(true).intValue() :0))
                .expectedTotalCount(mapSignature(membershipMap.getKey().getSetId()).getExpectedTotalCount())
                .build();
    }

    private MatchResponse mapSignature(Integer setId) {
        if (!setSignatureMap.containsKey(setId)) {
            List<Membership> allByFuzzySetId = membershipRepository.findAllByFuzzySetId(setId);
            setSignatureMap.put(setId,
                    MatchResponse.builder().expectedTotalCount(allByFuzzySetId.size())
                    .expectedRequiredCount((int) allByFuzzySetId.stream().filter(Membership::getRequired).count()).build());
        }
        return setSignatureMap.get(setId);
    }
}

package com.enumahin.cdss.service;

import com.enumahin.cdss.model.Degree;
import com.enumahin.cdss.model.Equality;
import com.enumahin.cdss.model.Membership;
import com.enumahin.cdss.model.dto.MatchList;
import com.enumahin.cdss.model.dto.MatchResponse;
import com.enumahin.cdss.repository.MembershipRepository;
import jakarta.transaction.Transactional;
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

    // Map<SetId, Map<TotalMemberCount, RequiredMemberCount>
    private Map<Integer, MatchResponse> setSignatureMap = new HashMap<>();

    public MembershipService(MembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    @Transactional
    public Membership addMembership(Membership membership){
        return membershipRepository.save(membership);
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

    public Optional<Membership> getById(Integer id) {
        return membershipRepository.findById(id);
    }

    public List<Membership> getBySetId(Integer id) {
        return membershipRepository.findAllByFuzzySetId(id);
    }

    public List<Membership> getByMemberId(Integer id) {
        return membershipRepository.findAllByMemberId(id);
    }

    public Map<Membership, Map<Boolean, Long>> match(MatchList matchList) {
       return matchList.getMatchList().stream()
               .map(match -> membershipRepository.equalTo(match.getMemberId(), match.getDegree().label))
               .flatMap(List::stream)
//               .map(set -> set.getSet().getSetName())
               .collect(Collectors.groupingBy(Function.identity(), Collectors.groupingBy(Membership::getRequired, Collectors.counting())));
    }

    public List<MatchResponse> matchStructure(MatchList matchList){
        return match(matchList).entrySet().stream()
                .flatMap(membershipMap -> Stream.of(MatchResponse.builder()
                                            .setId(membershipMap.getKey().getSet().getSetId())
                                            .setName(membershipMap.getKey().getSet().getSetName())
                                            .actualRequiredCount(
                                                    membershipMap.getValue().get(true) != null ? membershipMap.getValue().get(true).intValue() :0)
                                            .expectedRequiredCount(mapSignature(membershipMap.getKey().getSet().getSetId()).getExpectedRequiredCount())
                                            .actualTotalCount(
                                                    (membershipMap.getValue().get(false) != null ? membershipMap.getValue().get(false).intValue() : 0) +
                                                    (membershipMap.getValue().get(true) != null ? membershipMap.getValue().get(true).intValue() :0))
                                            .expectedTotalCount(mapSignature(membershipMap.getKey().getSet().getSetId()).getExpectedTotalCount())
                                            .build())
                                            ).toList();

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

package com.enumahin.cdss.service;

import com.enumahin.cdss.helpers.ExcelHelper;
import com.enumahin.cdss.model.*;
import com.enumahin.cdss.model.dto.*;
import com.enumahin.cdss.repository.FuzzySetRepository;
import com.enumahin.cdss.repository.MembershipRepository;
import com.enumahin.cdss.repository.SetMemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
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
//        System.out.println(match);
        return match.entrySet().stream()
                .map(this::fromMembership).toList();

    }

    private MatchResponse fromMembership(Map.Entry<FuzzySet, Map<Boolean, Long>> membershipMap){
//        System.out.println(membershipMap);
         return MatchResponse.builder()
                .setId(membershipMap.getKey().getSetId())
                .setName(membershipMap.getKey().getSetName())
                .actualRequiredCount(
                        membershipMap.getValue().get(true) != null ? membershipMap.getValue().get(true).intValue() :0)
                .expectedRequiredCount(mapSignature(membershipMap.getKey().getSetId()).getExpectedRequiredCount())
                .actualTotalCount(
                        Math.toIntExact(membershipMap.getValue().values().stream().filter(Objects::nonNull).reduce(Long::sum).get())
                )
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

    public List<MatchResponse> processBatch(MultipartFile linelist){

            if (ExcelHelper.hasExcelFormat(linelist)) {
                try {
                    List<BatchInput> batch = ExcelHelper.excelToGoods(linelist.getInputStream());

                    return getMatchResponses(batch);
                } catch (Exception exc) {
                    throw new IllegalArgumentException(exc);
                }

            } else {
                throw new IllegalArgumentException("File is not a valid excel file.");
            }
    }

    public List<MatchResponse> getMatchResponses(List<BatchInput> batch) {
        return batch.stream()
//                .filter(BatchInput::isEligible)
                .map(this::structureMatchList)
                .flatMap(mL -> matchStructure(mL).stream().peek(response -> {
                    response.setClientId(mL.getMatchList().get(0).getClientId());
                    response.setExpectedActivities(mL.getMatchList().get(0).getExpectedResult());
                    response.setTotalMemberMatchPercentage((int) (response.getActualTotalCount().floatValue() / response.getExpectedTotalCount().floatValue() * 100));
                    response.setRequiredMemberMatchPercentage((int) (response.getActualRequiredCount().floatValue() / response.getExpectedRequiredCount().floatValue() * 100));
                    response.setExpectedCounseling(mL.getMatchList().stream().map(Match::getExpectedResult).collect(Collectors.joining(",")).contains("2")? "Yes" : "No");
                    response.setExpectedRegimenPlan(mL.getMatchList().stream().map(Match::getExpectedResult).collect(Collectors.joining(",")).contains("3")? "Switch Regimen" : "Maintain Regimen");
                    response.setProposedRegimenPlan(response.getSetName().contains("Switch") ? "Switch Regimen" : "Maintain Regimen");
                    response.setProposedCounseling(response.getSetName().contains("Adherence")? "Yes" : "No");
                    System.out.println(response);
                }))
//                            .filter(mR -> mR.getActualTotalCount() / mR.getExpectedTotalCount() * 100 >= 75
//                                        && mR.getActualRequiredCount() / mR.getExpectedRequiredCount() * 100 >= 75)
//                .collect(Collectors.groupingBy(MatchResponse::getClientId))
//                .entrySet().stream().map(k -> {
//                    System.out.println(k);
//                    return buildResponse(k.getValue());
//                })
                .collect(Collectors.toList());
    }

    private MatchResponse buildResponse(List<MatchResponse> response){
//        System.out.println(response.get(0));
        MatchResponse matchResponse = new MatchResponse();
        matchResponse.setClientId(response.get(0).getClientId());
        matchResponse.setExpectedActivities(response.get(0).getExpectedActivities());
        matchResponse.setExpectedCounseling(response.get(0).getExpectedActivities().contains("2")? "Yes" : "No");
        matchResponse.setExpectedTotalCount(response.get(0).getExpectedTotalCount());
        matchResponse.setExpectedRequiredCount(response.get(0).getExpectedRequiredCount());
        matchResponse.setExpectedRegimenPlan(response.get(0).getExpectedActivities().contains("3")? "Switch Regimen" : "Maintain Regimen");

        matchResponse.setActualTotalCount(response.get(0).getActualTotalCount());
        matchResponse.setActualRequiredCount(response.get(0).getActualRequiredCount());
        matchResponse.setProposedRegimenPlan(response.stream().anyMatch(r -> r.getSetName().contains("Switch")) ? "Switch Regimen" : "Maintain Regimen");
        matchResponse.setProposedCounseling(response.stream().anyMatch(r -> r.getSetName().contains("Adherence"))? "Yes" : "No");
        return matchResponse;
    }

    private MatchList structureMatchList(BatchInput batchInput){
        MatchList matchList = new MatchList();
        Match match = new Match(batchInput.getClientId(), 1, 1d,
                batchInput.getExpectedResult());
        Match greaterThan50 = new Match(batchInput.getClientId(), 3, 1d,
                batchInput.getExpectedResult());
        Match lessThan1000 = new Match(batchInput.getClientId(), 2, 1d,
                batchInput.getExpectedResult());
        Match greaterThan1000 = new Match(batchInput.getClientId(), 4, 1d,
                batchInput.getExpectedResult());
        matchList.getMatchList().add(match);
        if (batchInput.getVlCount() < 1000){
            if(batchInput.getVlCount() >= 50 ){
                matchList.getMatchList().add(greaterThan50);
            }
            matchList.getMatchList().add(lessThan1000);
        } else {
            matchList.getMatchList().add(greaterThan50);
            matchList.getMatchList().add(greaterThan1000);
        }
        // System.out.println(matchList);
        return matchList;
    }
}

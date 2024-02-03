package com.enumahin.cdss.config;

import com.enumahin.cdss.model.*;
import com.enumahin.cdss.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class BootStrap implements CommandLineRunner {

    private final DegreeRepository degreeRepository;
    private final DegreeLevelRepository degreeLevelRepository;
    private final FuzzySetRepository fuzzySetRepository;
    private final MembershipRepository membershipRepository;
    private final MemberDegreeRepository memberDegreeRepository;
    private final SetMemberRepository setMemberRepository;

    public BootStrap(DegreeRepository degreeRepository,
                     DegreeLevelRepository degreeLevelRepository, FuzzySetRepository fuzzySetRepository,
                     MembershipRepository membershipRepository,
                     MemberDegreeRepository memberDegreeRepository,
                     SetMemberRepository setMemberRepository) {
        this.degreeRepository = degreeRepository;
        this.degreeLevelRepository = degreeLevelRepository;
        this.fuzzySetRepository = fuzzySetRepository;
        this.membershipRepository = membershipRepository;
        this.memberDegreeRepository = memberDegreeRepository;
        this.setMemberRepository = setMemberRepository;
    }

    @Override
    public void run(String... args) throws Exception {
//        if(! degreeRepository.findAll()
//                .iterator()
//                .hasNext()
//        ){
//            Double[] degrees = {0d, 0.25d, 0.5d, 0.75d, 1d};
//            Arrays.stream(degrees).forEach(d -> degreeRepository.save(Degree.builder().degree(d).build()));
//        }
//
//        if(! degreeLevelRepository.findAll().iterator().hasNext()){
//            String[] levels = {"L", "LE", "E", "G", "GE"};
//            Arrays.stream(DegreeEnum.values()).forEach(l -> degreeLevelRepository.save(DegreeLevel.builder().level(l.label).build()));
//        }

       if(! fuzzySetRepository.findAll().iterator().hasNext()){
           String[] fuzzySets = {"Enhanced Adherence Counseling", "Presumptive TB"};
           Arrays.stream(fuzzySets).forEach(fuzzySet -> fuzzySetRepository.save(FuzzySet.builder().setName(fuzzySet).build()));
       }

       if(! setMemberRepository.findAll().iterator().hasNext()){
           String[] members = {"Last Viral Load Status", "Last Viral Load Count", "Current Regimen Duration", "Gender", "Pregnancy Status"};
           Arrays.stream(members).forEach(member -> setMemberRepository.save(SetMember.builder().memberName(member).build()));
       }

       if(! memberDegreeRepository.findAll().iterator().hasNext()){
           Optional<SetMember> member = setMemberRepository.findById(1);
           member.ifPresent( m -> {
               memberDegreeRepository.save(
                       MemberDegree.builder()
                               .degree(DegreeEnum.D1)
                               .memberId(m)
                               .description("Unsuppressed = 0")
                               .build());
               memberDegreeRepository.save(
                           MemberDegree.builder()
                                   .degree(DegreeEnum.D5)
                                   .memberId(m)
                                   .description("Suppressed = 1")
                                   .build());
          });

       }
       if(! membershipRepository.findAll().iterator().hasNext()){
           Optional<FuzzySet> fuzzySet = fuzzySetRepository.findById(1);
           Optional<SetMember> member = setMemberRepository.findById(1);
           member.ifPresent(m ->
               fuzzySet.ifPresent( set ->
                   membershipRepository.save(
                           Membership.builder()
                                   .set(set)
                                   .member(m)
                                   .degreeOfMembership(DegreeEnum.D1)
                                   .equality(Equality.EQ)
                                   .build()
                   )
               )
           );

       }

    }
}

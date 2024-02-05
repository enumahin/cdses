package com.enumahin.cdss.config;

import com.enumahin.cdss.model.*;
import com.enumahin.cdss.repository.*;
import com.enumahin.cdss.service.MemberDegreeService;
import com.enumahin.cdss.service.MembershipService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class BootStrap implements CommandLineRunner {

    private final FuzzySetRepository fuzzySetRepository;
    private final MembershipRepository membershipRepository;
    private final MemberDegreeRepository memberDegreeRepository;
    private final SetMemberRepository setMemberRepository;

    public BootStrap(FuzzySetRepository fuzzySetRepository,
                     MembershipRepository membershipRepository,
                     MemberDegreeRepository memberDegreeRepository,
                     SetMemberRepository setMemberRepository) {
        this.fuzzySetRepository = fuzzySetRepository;
        this.membershipRepository = membershipRepository;
        this.memberDegreeRepository = memberDegreeRepository;
        this.setMemberRepository = setMemberRepository;
    }

    @Override
    public void run(String... args) throws Exception {
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
                               .degree(Degree.D1.label)
                               .memberId(m)
                               .description("Unsuppressed = 0")
                               .build());
               memberDegreeRepository.save(
                           MemberDegree.builder()
                                   .degree(Degree.D5.label)
                                   .memberId(m)
                                   .description("Suppressed = 1")
                                   .build());
          });

       }
       if(! membershipRepository.findAll().iterator().hasNext()){
           Optional<FuzzySet> fuzzySet = fuzzySetRepository.findById(1);
           Optional<SetMember> member = setMemberRepository.findById(1);

           member.ifPresent(m ->

               fuzzySet.ifPresent( set -> {
                   System.out.println("Member " + m);
                           if (membershipRepository.equalTo(m.getMemberId(), Degree.D1.label).isEmpty())
                               membershipRepository.save(
                                       Membership.builder()
                                               .set(set)
                                               .member(m)
                                               .degreeOfMembership(Degree.D1.label)
                                               .required(true)
                                               .equality(Equality.EQ)
                                               .build()
                               );
                       }
               )

           );

       }

    }
}

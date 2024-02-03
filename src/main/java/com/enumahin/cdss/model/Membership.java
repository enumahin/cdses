package com.enumahin.cdss.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "memberships")
@Builder
public class Membership {

    @Id
    @Column(name = "membership_id")
    private Integer membershipId;

    @OneToOne
    private FuzzySet set;

    @OneToOne
    private SetMember member;

    @Column(name = "degree_of_membership")
    private DegreeEnum degreeOfMembership;
//
//    @OneToOne
//    @JoinColumn(name = "degree_level")
//    private DegreeLevel degreeLevel;
    @Column(name = "equality")
    private Equality equality;
}

package com.enumahin.cdss.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "memberships")
@Builder
public class Membership {

    @Id
    @Column(name = "membership_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer membershipId;

    @ManyToOne()
    private FuzzySet set;

    @ManyToOne
    private SetMember member;

    @Column(name = "degree_of_membership")
    private Double degreeOfMembership;

    @Column()
    private Boolean required = false;
//
//    @OneToOne
//    @JoinColumn(name = "degree_level")
//    private DegreeLevel degreeLevel;
    @Column(name = "equality")
    private Equality equality;
}

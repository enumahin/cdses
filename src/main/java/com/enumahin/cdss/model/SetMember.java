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
@Table(name = "set_members")
@Builder
public class SetMember {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer memberId;

    @Column(name = "member_name", unique = true)
    private String memberName;

    @Column(name = "member_description")
    private String memberDescription;

    @ManyToOne
    @JoinColumn(name = "set_id")
    private FuzzySet fuzzySet;
}

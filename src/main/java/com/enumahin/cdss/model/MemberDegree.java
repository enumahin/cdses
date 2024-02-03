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
@Table(name = "member_degrees")
@Builder
public class MemberDegree {

    @Id
    @Column(name = "member_degree_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer memberDegreeId;

    @JoinColumn(name = "member_id")
    @ManyToOne
    private SetMember memberId;

    @Column(name = "degree")
    private Double degree;

    @Column(name = "description")
    private String description;

}

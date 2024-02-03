package com.enumahin.cdss.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "fuzzy_sets")
@Builder
public class FuzzySet {

    @Id
    @Column(name = "set_id")
    private Integer setId;

    @Column(name = "set_name")
    private String setName;

    @Column(name ="set_description")
    private String setDescription;

    @OneToMany
    private Set<SetMember> members;
}

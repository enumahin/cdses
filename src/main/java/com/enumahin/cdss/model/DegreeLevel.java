package com.enumahin.cdss.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "degree_levels")
@Builder
public class DegreeLevel {

    @Id
    @Column(name = "level_id")
    private Integer levelId;

    private String level;
}

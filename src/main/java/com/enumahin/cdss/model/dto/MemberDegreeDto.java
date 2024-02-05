package com.enumahin.cdss.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDegreeDto {

    private Integer memberDegreeId;

    private Integer memberId;

    private Double degree;

    private String description;

}

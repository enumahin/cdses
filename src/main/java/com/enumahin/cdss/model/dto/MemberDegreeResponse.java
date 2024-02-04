package com.enumahin.cdss.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDegreeResponse {

    private Integer memberDegreeId;

    private Integer memberId;

    private String memberName;

    private Double degreeOfMembership;

    private String degreeDescription;

}

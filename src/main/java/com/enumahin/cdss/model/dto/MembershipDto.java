package com.enumahin.cdss.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MembershipDto {

    private Integer fuzzySetId;

    private Integer memberId;

    private Double degree;

    private Boolean required = false;
}

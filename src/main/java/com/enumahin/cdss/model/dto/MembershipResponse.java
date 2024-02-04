package com.enumahin.cdss.model.dto;

import com.enumahin.cdss.model.Equality;
import com.enumahin.cdss.model.FuzzySet;
import com.enumahin.cdss.model.SetMember;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MembershipResponse {

    private Integer membershipId;

    private Integer setId;

    private String setName;

    private Integer memberId;

    private String memberName;

    private Double degreeOfMembership;

    private String degreeDescription;

    private Boolean required = false;

}

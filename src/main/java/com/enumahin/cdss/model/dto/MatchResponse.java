package com.enumahin.cdss.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchResponse {

    private Integer setId;

    private String clientId;

    private String setName;

    private Integer expectedRequiredCount;

    private Integer actualRequiredCount;

    private Integer expectedTotalCount;

    private Integer actualTotalCount;

    private String proposedCounseling;

    private String proposedRegimenPlan;

    private String expectedCounseling;

    private String expectedRegimenPlan;

    private String expectedActivities;

    private int totalMemberMatchPercentage;

    private int requiredMemberMatchPercentage;
}

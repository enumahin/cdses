package com.enumahin.cdss.model.dto;

import com.enumahin.cdss.model.Degree;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Match {
    private Integer memberId;
    private Degree degree;
}

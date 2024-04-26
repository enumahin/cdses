package com.enumahin.cdss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BatchInput {

    private String clientId;

    private boolean eligible;

    private int vlCount;

    private String expectedResult;
}

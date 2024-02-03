package com.enumahin.cdss.service;

import com.enumahin.cdss.model.Degree;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DegreeTest {

    // Add Degree Test
    @Test
    @DisplayName("Add Degree Test")
    void givenDegreeObjectWhenCalledAddDegreeThenReturnADegreeWithID(){
        // given
            Degree degree = Degree.builder()
                    .degree(0d)
                    .build();

        // when


        // then
    }
}

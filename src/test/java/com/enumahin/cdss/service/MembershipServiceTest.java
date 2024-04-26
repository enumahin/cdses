package com.enumahin.cdss.service;

import com.enumahin.cdss.helpers.ExcelHelper;
import com.enumahin.cdss.model.BatchInput;
import com.enumahin.cdss.model.dto.MatchResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.mockito.Mockito.mock;

public class MembershipServiceTest {


    @Test
    public void testEligibility(){
        try {
            // given
            String filename = "LineList.xlsx";
            File file = new File(filename);
            InputStream inputStream = new FileInputStream(file);
            MembershipService membershipService = mock(MembershipService.class);

            // when
            List<BatchInput> batchInputs = ExcelHelper.excelToGoods(inputStream);
            List<MatchResponse> matchResponses = membershipService.getMatchResponses(batchInputs);
            System.out.println(matchResponses);
            Assertions.assertEquals(9, batchInputs.size());
        } catch (IOException ex){
            ex.printStackTrace();
        }

    }

    @Test
    public void testMatchStructure() {

    }
}

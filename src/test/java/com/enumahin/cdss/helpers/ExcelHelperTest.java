package com.enumahin.cdss.helpers;

import com.enumahin.cdss.model.BatchInput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

public class ExcelHelperTest {

    @Test
    public void testExcelToGoods(){
        try {
            String filename = "LineList.xlsx";
            File file = new File(filename);
            InputStream inputStream = new FileInputStream(file);

            List<BatchInput> batchInputs = ExcelHelper.excelToGoods(inputStream);

            Assertions.assertEquals(9, batchInputs.size());
        } catch (IOException ex){
            ex.printStackTrace();
        }

    }

}

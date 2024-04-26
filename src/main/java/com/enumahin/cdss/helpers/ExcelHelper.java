package com.enumahin.cdss.helpers;

import com.enumahin.cdss.model.BatchInput;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static boolean hasExcelFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static List<BatchInput> excelToGoods(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            List<BatchInput> batchInputList = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                BatchInput batchInput = new BatchInput();

                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (currentCell.getColumnIndex()) {
                        case 0 -> batchInput.setClientId("CLIENT-"+rowNumber);
                        case 1 -> batchInput.setEligible(LocalDate.parse(currentCell.getStringCellValue(), formatter)
                                .isBefore(LocalDate.now().minusMonths(6)));
                        case 2 -> {
                            int vlCount = (int) currentCell.getNumericCellValue();
                            batchInput.setVlCount(vlCount);
                            if (vlCount >= 50) {
                                batchInput.setExpectedResult("2");
                                if (vlCount < 1000) {
                                    batchInput.setExpectedResult(batchInput.getExpectedResult() + ",1");
                                } else {
                                    batchInput.setExpectedResult(batchInput.getExpectedResult() + ",3");
                                }
                            } else {
                                batchInput.setExpectedResult("1");
                            }
                        }
                        default -> {
                        }
                    }

                }
                batchInputList.add(batchInput);
                rowNumber++;
            }

            workbook.close();

            return batchInputList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}

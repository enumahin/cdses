package com.enumahin.cdss.controller;

import com.enumahin.cdss.helpers.ExcelHelper;
import com.enumahin.cdss.model.*;
import com.enumahin.cdss.model.dto.MatchList;
import com.enumahin.cdss.model.dto.MatchResponse;
import com.enumahin.cdss.model.dto.MembershipDto;
import com.enumahin.cdss.model.dto.MembershipResponse;
import com.enumahin.cdss.service.MemberDegreeService;
import com.enumahin.cdss.service.MembershipService;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("memberships")
public class MembershipController {

    private final MembershipService membershipService;

    public MembershipController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    @GetMapping
    public List<MembershipResponse> getAll(){
        return membershipService.build();
    }

    @PostMapping(value = "/add")
    public ResponseEntity<Membership> create(@RequestBody MembershipDto membershipDto) {
        return new ResponseEntity<>(membershipService.addMembership(membershipDto), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Membership> findById(@PathVariable("id") Integer id) {
        return membershipService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("set/{id}")
    public List<Membership> findBySetId(@PathVariable("id") Integer id) {
        return membershipService.getBySetId(id);
    }

    @GetMapping("member/{id}")
    public List<Membership> findByMemberId(@PathVariable("id") Integer id) {
        return membershipService.getByMemberId(id);
    }

    @GetMapping("member/{memberId}/degree/{degree}equality/{equality}")
    public List<Membership> findByMemberId(@PathVariable Integer memberId, @PathVariable Equality equality, @PathVariable Degree degree) {
        return membershipService.getMembership(memberId, degree, equality);
    }

    @PostMapping("match")
    public List<MatchResponse> match(@RequestBody MatchList matchList) {
        System.out.println(matchList);
        return membershipService.matchStructure(matchList);
    }

    @PostMapping(value = {"match-batch"})
    public List<MatchResponse> saveImports(@RequestParam("linelist") MultipartFile linelist) {
         List<MatchResponse> responses = membershipService.processBatch(linelist);
         return responses;
//        try {
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            XSSFWorkbook workbook = createWorkBook(); // creates the workbook
//            HttpHeaders header = new HttpHeaders();
//            header.setContentType(new MediaType("application", "force-download"));
//            header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ProductTemplate.xlsx");
//            workbook.write(stream);
//            workbook.close();
//            return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()),
//                    header, HttpStatus.CREATED);
//        } catch (Exception e) {
////            log.error(e.getMessage());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
    }

//    private XSSFWorkbook createWorkBook() {
//        // Finds the workbook instance for XLSX file
//        XSSFWorkbook myWorkBook = new XSSFWorkbook ();
//
//        // Return first sheet from the XLSX workbook
//        XSSFSheet mySheet = myWorkBook.getSheetAt(0);
//
//        Set<String> newRows = data.keySet(); // get the last row number to append new data
//        int rownum = mySheet.getLastRowNum();
//        for (String key : newRows) {
//            // Creating a new Row in existing XLSX sheet
//            Row row = mySheet.createRow(rownum++); Object [] objArr = data.get(key);
//            int cellnum = 0;
//            for (Object obj : objArr) {
//                Cell cell = row.createCell(cellnum++);
//                if (obj instanceof String) {
//                    cell.setCellValue((String) obj);
//                } else if (obj instanceof Boolean) {
//                    cell.setCellValue((Boolean) obj);
//                } else if (obj instanceof Date) {
//                    cell.setCellValue((Date) obj);
//                } else if (obj instanceof Double) {
//                    cell.setCellValue((Double) obj);
//                }
//            }
//        }
//        // open an OutputStream to save written data into XLSX file
//        FileOutputStream os = new FileOutputStream(myFile);
//        myWorkBook.write(os);
//
//    }


}

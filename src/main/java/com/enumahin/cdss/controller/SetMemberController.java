package com.enumahin.cdss.controller;

import com.enumahin.cdss.model.FuzzySet;
import com.enumahin.cdss.model.SetMember;
import com.enumahin.cdss.service.FuzzySetService;
import com.enumahin.cdss.service.SetMemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("set_members")
public class SetMemberController {

    private final SetMemberService setMemberService;

    public SetMemberController(SetMemberService setMemberService) {
        this.setMemberService = setMemberService;
    }

    @GetMapping
    public List<SetMember> getSets(){
        return setMemberService.findAll();
    }

    @PostMapping
    public ResponseEntity<SetMember> create(@RequestBody SetMember setMember) {
        return new ResponseEntity<>(setMemberService.addMember(setMember), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<SetMember> create(@PathVariable("id") Integer memberId) {
        return setMemberService.findById(memberId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

//    @GetMapping("set/{id}")
//    public ResponseEntity<List<SetMember>> findMembersBySet(@PathVariable("id") Integer setId) {
//        return setMemberService.findBySetId(setId)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }

}

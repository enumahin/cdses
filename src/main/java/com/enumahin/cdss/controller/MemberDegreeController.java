package com.enumahin.cdss.controller;

import com.enumahin.cdss.model.MemberDegree;
import com.enumahin.cdss.model.SetMember;
import com.enumahin.cdss.service.MemberDegreeService;
import com.enumahin.cdss.service.SetMemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("member_degrees")
public class MemberDegreeController {

    private final MemberDegreeService memberDegreeService;

    public MemberDegreeController(MemberDegreeService memberDegreeService) {
        this.memberDegreeService = memberDegreeService;
    }

    @GetMapping
    public List<MemberDegree> getAll(){
        return memberDegreeService.findAll();
    }

    @PostMapping
    public ResponseEntity<MemberDegree> create(@RequestBody MemberDegree memberDegree) {
        return new ResponseEntity<>(memberDegreeService.addMemberDegree(memberDegree), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<MemberDegree> findById(@PathVariable("id") Integer id) {
        return memberDegreeService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("member/{id}")
    public List<MemberDegree> findByMemberId(@PathVariable("id") Integer id) {
        return memberDegreeService.getByMemberId(id);
    }

//    @GetMapping("set/{id}")
//    public ResponseEntity<List<SetMember>> findMembersBySet(@PathVariable("id") Integer setId) {
//        return setMemberService.findBySetId(setId)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }

}

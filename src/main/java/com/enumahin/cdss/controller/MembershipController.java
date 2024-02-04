package com.enumahin.cdss.controller;

import com.enumahin.cdss.model.*;
import com.enumahin.cdss.model.dto.MatchList;
import com.enumahin.cdss.model.dto.MatchResponse;
import com.enumahin.cdss.service.MemberDegreeService;
import com.enumahin.cdss.service.MembershipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("memberships")
public class MembershipController {

    private final MembershipService membershipService;

    public MembershipController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    @GetMapping
    public List<Membership> getAll(){
        return membershipService.findAll();
    }

    @PostMapping
    public ResponseEntity<Membership> create(@RequestBody Membership membership) {
        return new ResponseEntity<>(membershipService.addMembership(membership), HttpStatus.CREATED);
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

    @GetMapping("match")
    public List<MatchResponse> match(@RequestBody MatchList matchList) {
        return membershipService.matchStructure(matchList);
    }

}

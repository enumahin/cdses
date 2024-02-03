package com.enumahin.cdss.service;

import com.enumahin.cdss.model.SetMember;
import com.enumahin.cdss.repository.SetMemberRepository;
import org.springframework.stereotype.Service;

import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Optional;

@Service
public class SetMemberService {

    private final SetMemberRepository setMemberRepository;

    public SetMemberService(SetMemberRepository setMemberRepository) {
        this.setMemberRepository = setMemberRepository;
    }

    public SetMember addMember(SetMember setMember){
        return setMemberRepository.save(setMember);
    }

    public List<SetMember> findAll() {
        return setMemberRepository.findAll();
    }
    public Optional<SetMember> findById(Integer id) {
        return setMemberRepository.findById(id);
    }
//
//    public List<SetMember> findBySetId(Integer setId) {
//        return setMemberRepository.findById(setId);
//    }
}

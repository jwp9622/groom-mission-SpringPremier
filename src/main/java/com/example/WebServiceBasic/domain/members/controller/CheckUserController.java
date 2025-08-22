package com.example.WebServiceBasic.domain.members.controller;

import com.example.WebServiceBasic.domain.members.service.MemberItemCheck;
import com.example.WebServiceBasic.domain.members.service.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class CheckUserController {

    @Autowired
    MemberItemCheck memberItemCheck;

    //목록 get
    @GetMapping("/api/userscheck-userid/{userId}")
    public ResponseEntity<Map<String, Boolean>> checkUserId(@PathVariable String userId){

        Boolean exists = memberItemCheck.checkUserId(userId);

        //true 존재함, false 존재안함.
        return ResponseEntity.ok(Map.of("exists", exists));
    }


}

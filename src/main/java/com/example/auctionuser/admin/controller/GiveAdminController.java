package com.example.auctionuser.admin.controller;

import com.example.auctionuser.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@Log4j2
@RestController
public class GiveAdminController {

    private final AdminService adminService;

    @PostMapping(value = "give-admin")
    public ResponseEntity giveMeAdmin(@RequestBody Map<String,Object> passwordMap,
                                      Authentication authentication){
        int resultNum = adminService.giveAdmin(authentication,passwordMap);

        if(resultNum ==1){
            return new ResponseEntity("success admin auth give", HttpStatus.OK);
        }else{
            return new ResponseEntity("fail admin give",HttpStatus.FORBIDDEN);
        }
    }
}

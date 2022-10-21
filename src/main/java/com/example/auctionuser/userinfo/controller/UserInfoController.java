package com.example.auctionuser.userinfo.controller;

import com.example.auctionuser.config.auth.PrincipalDetails;
import com.example.auctionuser.service.UserService;
import com.example.modulecommon.frontModel.UserModelFront;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Log4j2
@RequestMapping(value = "user")
@RestController
public class UserInfoController {

    private final UserService userService;

    @GetMapping(value = "info")
    public ResponseEntity findUserModelFront(Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();


        return new ResponseEntity(userService.findUserNameFrontUserModel(principalDetails.getUsername(),true), HttpStatus.OK);
    }

    @GetMapping(value = "info/{userid}")
    public ResponseEntity<UserModelFront> findUserModelFront(@PathVariable int userid){

        return new ResponseEntity<>(userService.findUserNameFrontUserModel(Integer.toString(userid),false), HttpStatus.OK);
    }
}

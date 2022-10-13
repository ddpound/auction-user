package com.example.auctionuser.userinfo.controller;

import com.example.auctionuser.config.auth.PrincipalDetails;
import com.example.auctionuser.model.UserModelFront;
import com.example.auctionuser.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Log4j2
@RestController
public class UserInfoController {

    private final UserService userService;

    @GetMapping(value = "info")
    public ResponseEntity findUserModelFront(Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        log.info("-------------------- info controller On ------------------------");

        return new ResponseEntity(userService.findUserNameFrontUserModel(principalDetails.getUsername()), HttpStatus.OK);
    }
}

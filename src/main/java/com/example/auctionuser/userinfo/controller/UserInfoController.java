package com.example.auctionuser.userinfo.controller;

import com.example.auctionuser.config.auth.PrincipalDetails;
import com.example.auctionuser.service.UserService;
import com.example.modulecommon.frontModel.UserModelFront;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    @PostMapping(value = "save-address")
    public ResponseEntity<String> saveAddress(Authentication authentication,
                                              @RequestParam(value = "address")String address,
                                              @RequestParam(value = "addAddress")String addAddress){

        int resultNum = userService.saveAddress(authentication,address,addAddress);

        if(resultNum == 1){
            return new ResponseEntity<>("success save address" , HttpStatus.OK);
        }

        return new ResponseEntity<>("fail save" , HttpStatus.BAD_REQUEST);
    }

    /**
     * 구매 예약 버튼을 눌렀을 때
     * 주소가 있는지 확인해보는 엔드포인트
     * */
    @GetMapping(value = "check-address")
    public  ResponseEntity<String> checkAddress(Authentication authentication){

        return new ResponseEntity<>(userService.checkAddress(authentication) , HttpStatus.OK);
    }
}

package com.example.auctionuser.admin.controller;

import com.example.auctionuser.config.auth.PrincipalDetails;
import com.example.auctionuser.service.AdminService;
import com.example.modulecommon.enums.AdminBoardCategory;
import com.example.modulecommon.makefile.MakeFile;
import com.example.modulecommon.model.IntegrateBoardModel;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin")
public class AdminController {

    private final AdminService adminService1;

    private final MakeFile makeFile;

    @GetMapping(value = "info")
    public String setAdminUrl(){


        return "successAdmin";
    }

    /**
     * 관리자 권한을 주는 엔드포인트
     *
     * */
    @PostMapping(value = "give-admin")
    public ResponseEntity giveMeAdmin(@RequestBody Map<String,Object> passwordMap,
                                      Authentication authentication){
        int resultNum = adminService1.giveAdmin(authentication,passwordMap);

        if(resultNum ==1){
            return new ResponseEntity("success admin auth give",HttpStatus.OK);
        }else{
            return new ResponseEntity("fail admin give",HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping(value = "make-coupon")
    public ResponseEntity makeCoupon(@RequestParam(required = false)String couponNumber){


        adminService1.makeCoupon(couponNumber);


        return new ResponseEntity("success make coupon", HttpStatus.OK);
    }

    @GetMapping(value = "find-all-coupon")
    public ResponseEntity findAllCoupon(){

        return new ResponseEntity(adminService1.findAllCoupon(), HttpStatus.OK);
    }

    @DeleteMapping(value = "delete-one-coupon/{id}")
    public ResponseEntity deleteOneCoupon(@PathVariable("id")int id){

        adminService1.deleteCoupon(id);

        return new ResponseEntity("deleteSuccess", HttpStatus.OK);
    }


    // 어드민 권한으로 글 작성할 때 사진을 임시저장
    @PostMapping(value = "temporary-image-save", produces = "application/json")
    public JsonObject boardImageTemporarySave(
            @RequestParam("file") MultipartFile multipartFile,
            Authentication authentication,
            HttpServletRequest request){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();


        return makeFile.makeTemporaryfiles(multipartFile,principalDetails.getUserModel(),request);
    }

    @PostMapping(value = "save-announcement-board", produces = "application/json")
    public ResponseEntity saveAnnouncementBoard(@RequestBody Map<String,String> boardData,
                                                Authentication authentication,
                                                HttpServletRequest request){

        PrincipalDetails principalDetails =(PrincipalDetails) authentication.getPrincipal();


        // 세이브 서비스 붙여주기

        String folderPathName =  adminService1.saveAnnouncementBoardImageFIle(principalDetails.getUserModel().getUserId(),boardData.get("content"));
        adminService1.saveAnnouncementBoard(
                IntegrateBoardModel.builder()
                        .title(boardData.get("title"))
                        .Content(boardData.get("content"))
                        .userModel(principalDetails.getUserModel())
                        .adminBoardCategory(AdminBoardCategory.Announcemnet)
                        .build(),
                folderPathName,
                request
        );


        return new ResponseEntity<>("OK",HttpStatus.OK);
    }


}

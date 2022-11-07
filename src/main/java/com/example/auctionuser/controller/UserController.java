package com.example.auctionuser.controller;

import com.example.auctionuser.service.AuctionSellerBoardReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Log4j2
@RequiredArgsConstructor
@RequestMapping(value = "user")
@RestController
public class UserController {


    private final AuctionSellerBoardReplyService auctionSellerBoardReplyService;

    /**
     * 유저가 판매자의 댓글 작성에 사용할 엔드포인트
     * */
    @PostMapping("save-reply")
    public ResponseEntity saveSellerCommonBoardReply(HttpServletRequest request,
                                                     @RequestParam(value="content", required=false)String content,
                                                     @RequestParam(value="userId", required=false)int userId,
                                                     @RequestParam(value="nickName", required=false)String nickName,
                                                     @RequestParam(value="boardId", required=false)int boardId){
        System.out.println("여기까지 왜안옴?");
        int resultNum = auctionSellerBoardReplyService.saveReply(content,userId,nickName,boardId,request);

        if(resultNum == 1){
            return new ResponseEntity<String>("Success Save Reply", HttpStatus.OK);
        }

        return new ResponseEntity<String>("FailSaveReply", HttpStatus.BAD_REQUEST);
    }
}

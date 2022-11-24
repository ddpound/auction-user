package com.example.auctionuser.controller;

import com.example.auctionuser.service.AuctionSellerBoardReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> saveSellerCommonBoardReply(HttpServletRequest request,
                                                     @RequestParam(value="content", required=false)String content,
                                                     @RequestParam(value="userId", required=false)int userId,
                                                     @RequestParam(value="nickName", required=false)String nickName,
                                                     @RequestParam(value="userPicture", required=false)String userPicture,
                                                     @RequestParam(value="boardId", required=false)int boardId){

        if(content.length() ==0 || userId <0 || nickName.length() <= 0 || boardId < 0){
            log.info("request save Reply is null value");
            return new ResponseEntity<String>("null value", HttpStatus.BAD_REQUEST);
        }

        int resultNum = auctionSellerBoardReplyService.saveReply(content,userId,nickName,userPicture,boardId,request);

        if(resultNum == 1){
            return new ResponseEntity<String>("Success Save Reply", HttpStatus.OK);
        }

        return new ResponseEntity<String>("FailSaveReply", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping( value = "delete-reply/{id}")
    ResponseEntity<String> deleteReply(HttpServletRequest request,
                                       @PathVariable(value = "id")Integer replyId){
        int resultNum = auctionSellerBoardReplyService.deleteReply(replyId,request);

        if(resultNum == 1){
            return new ResponseEntity<String>("Success delete Reply", HttpStatus.OK);
        }

        return new ResponseEntity<String>("Fail Delete Reply", HttpStatus.BAD_REQUEST);
    }

    @PostMapping( value = "save-reply/of-reply")
    ResponseEntity<String> deleteReply(HttpServletRequest request,
                                       @RequestParam(value="content", required=false)String content,
                                       @RequestParam(value="userId", required=false)int userId,
                                       @RequestParam(value="nickName", required=false)String nickName,
                                       @RequestParam(value="userPicture", required=false)String userPicture,
                                       @RequestParam(value="boardId", required=false)int boardId,
                                       @RequestParam(value="replyId", required=false)int replyId){

        if(content.length() ==0 || userId <0 || nickName.length() <= 0 || boardId < 0){
            log.info("request save Reply is null value");
            return new ResponseEntity<String>("null value", HttpStatus.BAD_REQUEST);
        }

        int resultNum = auctionSellerBoardReplyService.saveReplyOfReply(content,userId,nickName,userPicture,boardId,replyId,request);

        if(resultNum == 1){
            return new ResponseEntity<String>("Success Save Reply", HttpStatus.OK);
        }

        return new ResponseEntity<String>("FailSaveReplyofReply", HttpStatus.BAD_REQUEST);
    }


    // 대댓글 삭제
    @DeleteMapping(value = "delete-reply/of-reply/{id}")
    ResponseEntity<String> deleteReplyOfReply(HttpServletRequest request,
                                              @PathVariable(value = "id")Integer replyId){
        int resultNum = auctionSellerBoardReplyService.deleteReplyOfReply(replyId,request);

        if(resultNum == 1){
            return new ResponseEntity<String>("Success delete Reply", HttpStatus.OK);
        }

        return new ResponseEntity<String>("Fail Delete Reply", HttpStatus.BAD_REQUEST);
    }


}

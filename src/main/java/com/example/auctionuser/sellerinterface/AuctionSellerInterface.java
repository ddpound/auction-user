package com.example.auctionuser.sellerinterface;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@FeignClient(name = "auction-seller")
public interface AuctionSellerInterface {

    @RequestMapping(method = RequestMethod.POST, value = "/seller/save-reply", consumes = "application/json")
    ResponseEntity<String> saveReply(@RequestHeader("Authorization")String token,
                                     @RequestHeader("RefreshToken") String reToken,
                                     @RequestParam(value="content", required=false)String content,
                                     @RequestParam(value="userId", required=false)int userId,
                                     @RequestParam(value="nickName", required=false)String nickName,
                                     @RequestParam(value="userPicture", required=false)String userPicture,
                                     @RequestParam(value="boardId", required=false)int boardId);

    @RequestMapping(method = RequestMethod.POST, value = "/seller/save-reply/of-reply", consumes = "application/json")
    ResponseEntity<String> saveReplyOfReply(@RequestHeader("Authorization")String token,
                                            @RequestHeader("RefreshToken") String reToken,
                                            @RequestParam(value="content", required=false)String content,
                                            @RequestParam(value="userId", required=false)int userId,
                                            @RequestParam(value="nickName", required=false)String nickName,
                                            @RequestParam(value="userPicture", required=false)String userPicture,
                                            @RequestParam(value="boardId", required=false)int boardId,
                                            @RequestParam(value="replyId", required=false)int replyId);
}

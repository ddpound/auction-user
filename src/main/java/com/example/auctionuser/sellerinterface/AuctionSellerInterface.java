package com.example.auctionuser.sellerinterface;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@FeignClient(name = "auction-seller")
public interface AuctionSellerInterface {

    @PostMapping(value = "/seller/save-reply")
    ResponseEntity<String> saveReply(@RequestHeader("Authorization")String token,
                                     @RequestHeader("RefreshToken") String reToken,
                                     HttpServletRequest request,
                                     @RequestParam(value="content", required=false)String content,
                                     @RequestParam(value="userId", required=false)int userId,
                                     @RequestParam(value="nickName", required=false)String nickName,
                                     @RequestParam(value="boardId", required=false)int boardId);
}

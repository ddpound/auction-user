package com.example.auctionuser.service;

import com.example.auctionuser.sellerinterface.AuctionSellerInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Log4j2
@RequiredArgsConstructor
@Service
public class AuctionSellerBoardReplyService {

    private final AuctionSellerInterface auctionSellerInterface;

    public int saveReply(String content,
                         int userId,
                         String nickName,
                         int commonModelId,
                         HttpServletRequest request){

        ResponseEntity responseValue =  auctionSellerInterface.saveReply(request,content,userId,nickName,commonModelId);

        if(responseValue.getStatusCode().value() == 200){
            return 1;
        }


        return -1;
    }
}

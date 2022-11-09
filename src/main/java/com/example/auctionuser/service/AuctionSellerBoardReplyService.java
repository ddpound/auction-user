package com.example.auctionuser.service;

import com.example.auctionuser.sellerinterface.AuctionSellerInterface;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
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
                         String userPicture,
                         int commonModelId,
                         HttpServletRequest request){

        try{
            String jwtHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            String jwtRHeader = request.getHeader("RefreshToken");

            String token = jwtHeader.replace("Bearer ", "");
            String reToken = jwtHeader.replace("Bearer ", "");

            ResponseEntity<String> responseValue =  auctionSellerInterface.saveReply(token,reToken,content,userId,nickName,userPicture,commonModelId);


            if(responseValue.getStatusCode().value() == 200){
                return 1;
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }

        return -1;
    }

    public int saveReplyOfReply(String content,
                         int userId,
                         String nickName,
                         String userPicture,
                         int commonModelId,
                         int replyId,
                         HttpServletRequest request){

        try{
            String jwtHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            String jwtRHeader = request.getHeader("RefreshToken");

            String token = jwtHeader.replace("Bearer ", "");
            String reToken = jwtHeader.replace("Bearer ", "");

            ResponseEntity<String> responseValue =  auctionSellerInterface.saveReplyOfReply(token,reToken,content,userId,nickName,userPicture,commonModelId,replyId);


            if(responseValue.getStatusCode().value() == 200){
                return 1;
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }

        return -1;
    }
}

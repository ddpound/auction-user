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
                         int commonModelId,
                         HttpServletRequest request){
        System.out.println("서비스까지 들어옴");



        try{
            String jwtHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            String jwtRHeader = request.getHeader("RefreshToken");

            String token = jwtHeader.replace("Bearer ", "");
            String reToken = jwtHeader.replace("Bearer ", "");

            ResponseEntity<String> responseValue =  auctionSellerInterface.saveReply(token,reToken,content,userId,nickName,commonModelId);

            System.out.println("여기까지 왜안옴?3");

            if(responseValue.getStatusCode().value() == 200){
                return 1;
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }

        return -1;
    }
}

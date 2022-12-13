package com.example.auctionuser.service;

import com.example.auctionuser.model.dto.ReservationDetails;
import com.example.auctionuser.sellerinterface.AuctionSellerInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Log4j2
@Service
public class ReservationService {

    private final AuctionSellerInterface auctionSellerInterface;


    public int saveReservation(HttpServletRequest request, ReservationDetails reservationDetails){

        String jwtHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String jwtRHeader = request.getHeader("RefreshToken");

        String token = jwtHeader.replace("Bearer ", "");
        String reToken = jwtHeader.replace("Bearer ", "");

        ResponseEntity<String> responseValue =  auctionSellerInterface.saveReservation(token,reToken,reservationDetails);

        if(responseValue.getStatusCode().value() == 200){
            return 1;
        }

        return -1;
    }
}

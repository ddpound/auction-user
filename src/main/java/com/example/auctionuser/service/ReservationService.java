package com.example.auctionuser.service;

import com.example.auctionuser.model.dto.ReservationDetails;
import com.example.auctionuser.sellerinterface.AuctionSellerInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Log4j2
@Service
public class ReservationService {

    private final AuctionSellerInterface auctionSellerInterface;


    public int saveReservation(HttpServletRequest request, ReservationDetails reservationDetails){


        return 1;
    }
}

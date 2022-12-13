package com.example.auctionuser.controller;

import com.example.auctionuser.model.dto.ReservationDetails;
import com.example.auctionuser.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RequiredArgsConstructor
@Log4j2
@RequestMapping(value = "user")
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("save-reservation")
    public ResponseEntity saveReservation(@RequestBody ReservationDetails reservationDetails,
                                          HttpServletRequest request){


        System.out.println(reservationDetails.getBuyerId());
        System.out.println(reservationDetails.getOptionList());
        System.out.println(reservationDetails.getProductId());
        System.out.println(reservationDetails);

        int result = reservationService.saveReservation(request,reservationDetails);

        if(result == 1){
            return new ResponseEntity("success save Reservation", HttpStatus.OK);
        }

        return new ResponseEntity("Sorry fail save Reservation", HttpStatus.BAD_REQUEST);
    }



}

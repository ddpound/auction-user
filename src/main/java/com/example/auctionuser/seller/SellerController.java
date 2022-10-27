package com.example.auctionuser.seller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@RestController
public class SellerController {

    private final SellerService sellerService;

    @PostMapping("give-seller")
    public ResponseEntity giveSeller(HttpServletRequest request, @RequestBody Map<String,Object> coupon){

        int resultNum = sellerService
                .sellerRegister(
                        request,
                        coupon.get("id").toString(),
                        coupon.get("code").toString());
        if(resultNum == 1 ){
            return new ResponseEntity(HttpStatus.OK);
        } else if (resultNum == -1) {
            return new ResponseEntity("JCODE001", HttpStatus.BAD_REQUEST);
        } else if (resultNum == -3) {
            return new ResponseEntity("JCODE010", HttpStatus.BAD_REQUEST);
        }else if (resultNum == -5) {
            return new ResponseEntity("JCODE800", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity("It's a coupon I've already used", HttpStatus.FORBIDDEN);
        }


    }

}

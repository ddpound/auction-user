package com.example.auctionuser.frontmodel;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SellerCouponFront {

    private int id;

    private String couponCode;

    private int userId;

    private String userName;


}

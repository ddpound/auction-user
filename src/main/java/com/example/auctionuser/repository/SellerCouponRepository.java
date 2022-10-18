package com.example.auctionuser.repository;


import com.example.auctionuser.model.SellerCoupon;

import com.example.auctionuser.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerCouponRepository extends JpaRepository<SellerCoupon,Integer> {

    SellerCoupon findByUserModel(UserModel userModel);

    SellerCoupon findByCouponPassword(String CouponPassword);


    // 아이디와 쿠폰번호가 같은 녀석을 찾아서 있다면 여기에 set을해줘서 등록해주자
    SellerCoupon findByIdAndCouponPassword(int id, String CouponPassword);

    void deleteByUserModel(UserModel userModel);

}
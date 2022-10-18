package com.example.auctionuser.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class SellerCoupon {


    // 쿠폰 고유번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // 쿠폰의 비밀번호
    // 넉넉하게
    @Column(length = 500)
    private String couponPassword;

    // 하나의 유저가 쿠폰을 가진 형태
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserModel userModel;

    @CreationTimestamp
    private Timestamp createCoupon;

    @UpdateTimestamp
    private Timestamp modifyCoupon;

}
package com.example.auctionuser.model;


import com.example.auctionuser.model.dto.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    // 구글등의 닉네임이 담길 예정
    @Column(nullable = false)
    private String nickname;

    // 이메일이 담길예정, 중복 불가
    // username이 이메일
    @Column(nullable = false)
    private String username;

    // 토큰값을 넣을까 고민중
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    // 어디 로그인api를 사용했는지도 구분해놔야함
    private String oauthname;
    @Column(nullable = false)
    private String roles; // USER, ADMIN, Seller

    // 썸네일 사진
    private String picture;

    // 유저가 관리하는 주소
    // address + , + addAddress
    private String address;

    public List<String> getRoleList(){
        if(this.roles.length() > 0){
            // , 로 스플릿 해서 배열로 리턴해준다
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }

    //최초 로그인 날짜와

    //가장 마지막으로 로그인한 날짜를 정해주자

    // 그리고 만료


}
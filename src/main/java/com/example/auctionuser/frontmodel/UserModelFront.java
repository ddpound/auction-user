package com.example.auctionuser.frontmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserModelFront {

    private int id;

    // 이메일
    private String userName;

    // 룰을 저장 (보통 ROLE_* 이기떄문에 ROLE_을 제거해주고 보내주자)
    // user -> 일반, seller -> 판매자, admin -> 관리자

    private String role;

    // 닉네임
    private String nickName;

    private String picture;

}
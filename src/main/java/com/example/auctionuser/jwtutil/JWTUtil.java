package com.example.auctionuser.jwtutil;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.auctionuser.model.UserModel;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.Collections;


/**
 * 해당 클래스는 의존성 주입이 필요한 클래스
 * 컴포넌트로 IOC에 등록 해줘야함
 * */
@Log4j2
@Component
public class JWTUtil {

    @Value("${googlelogin.googleClientId}")
    private String googleClientId;

    @Value("${googlelogin.googlekey}")
    private String googlekey;

    // @Value 는 정적 변수로는 담지 못함
    // 토큰 검증에 필요한 키
    @Value("${myToken.myKey}")
    private String myKey;

    @Value("${tokenVerifyTime.accesstimeSet}")
    private long AUTH_TIME;

    @Value("${tokenVerifyTime.refreshTimeSet}")
    private long REFRESH_TIME;

    /**
     * 토큰 제작 메소드
     *
     * */
    public String makeAuthToken(UserModel user){
        log.info("now New make Token : " + user.getUsername());
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuer("nowAuction")
                .withClaim("username", user.getUsername()) // 유저이름
                .withClaim("userRole",user.getRoleList())
                .withClaim("exp", Instant.now().getEpochSecond()+AUTH_TIME)
                .sign(Algorithm.HMAC256(myKey));

        // EpochSecond 에폭세컨드를 이용해 exp이름을 붙여 직접 시간을 지정해준다
    }

    /**
     * 유저네임을 넣은 Refresh  Token
     *
     * */
    public String makeRfreshToken(UserModel user){
        log.info("now New make refresh Token : " + user.getUsername());
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuer("nowAuction")
                .withClaim("refresh","refresh")
                .withClaim("exp", Instant.now().getEpochSecond()+REFRESH_TIME)
                .sign(Algorithm.HMAC256(myKey));

        // EpochSecond 에폭세컨드를 이용해 exp이름을 붙여 직접 시간을 지정해준다
        // 만료시간은 리프레쉬 토큰 시간에 맞춰서 넣는다
    }


    /**
     * 구글에서 성공적으로 받아온 토큰을 검증해주는 메소드
     * @param token 구글 로그인 성공 토큰을 넣어주세요
     * */
    public GoogleIdToken.Payload googleVerify(String token) throws GeneralSecurityException, IOException {

        if (googlekey ==null){
            log.info("googlekey null");
        }

        GoogleIdTokenVerifier googleIdTokenVerifier =
                new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                        .setAudience(Collections.singletonList(googleClientId))
                        .setIssuer("https://accounts.google.com")
                        .build();

        GoogleIdToken googleIdToken = googleIdTokenVerifier.verify(token);

        // 값이 나오면 인증 성공
        // 설명이 조금 부실하긴함 인증 하지만 암호화 검증이 제대로 이루어졌는지에 대한 궁금증
        if(googleIdToken != null){
            GoogleIdToken.Payload payload = googleIdToken.getPayload();

            log.info("Verification success");
            //log.info(payload.getSubject());
            //log.info(payload.getEmail());
            String name = (String) payload.get("name");
            log.info(name);

            return payload;

        }else{
            log.info("Verification failed");
            return null;
        }

    }



}

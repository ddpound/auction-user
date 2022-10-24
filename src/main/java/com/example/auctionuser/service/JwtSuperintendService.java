package com.example.auctionuser.service;

import com.example.auctionuser.model.UserModel;
import com.example.auctionuser.repository.UserModelRepository;
import com.example.modulecommon.jwtutil.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;


@Log4j2
@RequiredArgsConstructor
@Service
public class JwtSuperintendService {

    // 공통 모듈에 있는 JWTUtil
    private final JWTUtil jwtUtil;

    private final String url = "http://localhost:8000/saveCheckToken";

    // 레파지토리를 통해 토큰을 저장할대 사용
    // 이미있다면 수정도 해야함
    @Transactional
    public int saveCheckTokenRepository(String getUserName,
                                        String makeMyToken,
                                        String makeRefleshToken){

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        try {
            headers.setContentType(MediaType.APPLICATION_JSON);

//            headers.set("Authorization", makeMyToken);
//            headers.set("RefreshToken", makeRefleshToken);
//            headers.set("ServerToken", jwtUtil.makeServerAuthToken("username"));

            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("username", getUserName);
            map.add("mytoken", makeMyToken);
            map.add("RefreshToken", makeRefleshToken);
            map.add("ServerToken", jwtUtil.makeServerAuthToken("username"));



            HttpEntity request = new HttpEntity(map,headers);

            ResponseEntity response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            //System.out.println(response);
            // restTemplate를 이용해 localhost 8000 을 호출

        }catch (Exception e){
            log.info(e);
        }

        return 1;
    }

}

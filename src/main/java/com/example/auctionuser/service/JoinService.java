package com.example.auctionuser.service;

import com.example.auctionuser.jwtutil.JWTUtil;
import com.example.auctionuser.model.UserModel;
import com.example.auctionuser.repository.UserModelRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;


@RequiredArgsConstructor
@Log4j2
@Service
public class JoinService {

    @Value("${myToken.userSecretKey}")
    private String userSecretKey;

    private final UserModelRepository userModelRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JWTUtil jwtUtil;


    /**
     * 헤더에 있는 구글 토큰 값을 뽑아 회원가입 시키는 메소드
     * */
    @Transactional
    public int googleTokenJoinGetHeader(HttpServletRequest request){

        if(userSecretKey == null){
            log.info("TokenService Key is null");
        }

        String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(headerAuth != null){
            GoogleIdToken.Payload googlepayload = null;
            try {
                googlepayload = jwtUtil.googleVerify(headerAuth.substring("Bearer ".length()));
                UserModel userModel = userModelRepository.findByUsername(googlepayload.getEmail());

                if(userModel == null){
                    // 여기서 걸렸다는 건 로그인 시 최초가입이라는 뜻
                    log.info("This account does not exist");
                    UserModel joinUserModel = UserModel.builder()
                            .password(bCryptPasswordEncoder.encode(userSecretKey))
                            .roles("ROLE_USER")
                            .nickname((String) googlepayload.get("name"))
                            .picture((String) googlepayload.get("picture"))
                            .username(googlepayload.getEmail())
                            .oauthname("Google").build();

                    userModelRepository.save(joinUserModel); // save 임
                    log.info("save new user email: "+ googlepayload.get("email") );
                }

            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
                return -2;
            }
        }else{
          log.info("This request does not have header AUTHORIZATION");
        }

        return 1;
    }


}

package com.example.auctionuser.join.controller;

import com.example.auctionuser.config.auth.PrincipalDetails;
import com.example.auctionuser.jwtutil.UserJWTUtil;
import com.example.auctionuser.service.JWTCookieService;
import com.example.auctionuser.service.JoinService;
import com.example.auctionuser.service.JwtSuperintendService;
import com.example.auctionuser.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;


@Log4j2
@RequiredArgsConstructor
@RestController
public class JoinLoginController {

    private final UserService userService;

    private final JoinService joinService;

    private final UserJWTUtil userJwtUtil;

    private final JwtSuperintendService jwtSuperintendService;

    private final JWTCookieService jwtCookieService;

    @Value("${myToken.cookieJWTName}")
    private String JWT_COOKIE_NAME;

    @Value("${myToken.refreshJWTCookieName}")
    private String REFRESH_COOKIE_NAME;

    // 설마 Origin localhost가 아니라 ip내부주소로 인증되어서그런건가;;
    @GetMapping (value = "login/token/google")
    //@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = {"Authorization","RefreshToken","Content-Type"})
    public ResponseEntity loginTryGoogle(Authentication authentication , HttpServletResponse response) {

        try {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

            String makeMyToken = userJwtUtil.makeAuthToken(principalDetails.getUserModel());
            String makeRefleshToken = userJwtUtil.makeRfreshToken(principalDetails.getUserModel());

            //log.info("make Token : "+makeMyToken);
            //헤더에 담아 전송, 프론트에 전달
            response.addHeader("Authorization", "Bearer "+ makeMyToken);
            response.addHeader("RefreshToken","Bearer "+ makeRefleshToken);

            jwtSuperintendService.saveCheckTokenRepository(principalDetails.getUsername(),makeMyToken,makeRefleshToken);

//            response.addCookie(jwtCookieService.addTokenToCookie(response, makeMyToken, JWT_COOKIE_NAME));
//            response.addCookie(jwtCookieService.addTokenToCookie(response, makeMyToken, REFRESH_COOKIE_NAME));


            log.info("login success : " + principalDetails.getUsername());
            return new ResponseEntity<>(userService.findUserNameFrontUserModel(principalDetails.getUsername(),true) , HttpStatus.OK);
            //return "seuccess";
        }catch (NullPointerException e){
            log.info("principalDetails is null, LoginController");
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        // 처음 로그인할때 exchang 메소드를 이용해서 아이디 검증후 없다면
        // join문 실행해줘도 될듯

    }

    @PostMapping("join/googletoken")
    public ResponseEntity googleTokenJoin(HttpServletRequest request){


        int resultNum = joinService.googleTokenJoinGetHeader(request);

        if(resultNum == 1 ){
            log.info("join success");
            return new ResponseEntity<>( "Your membership registration is complete.", HttpStatus.OK);
        }else {
            return new ResponseEntity<>( "sorry fail join", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("logout/googletoken")
    public ResponseEntity<String> logoutDeleteCookie(HttpServletRequest request,
                                                     HttpServletResponse response){

        try {
            Cookie[] cookies = request.getCookies();

            ArrayList<Cookie> jwtCookieList = jwtCookieService.logoutCookieDelete(cookies,response);

            for (Cookie cookie : jwtCookieList
            ) {
                response.addCookie(cookie);
            }


            log.info("delete cookie");
            return new ResponseEntity<>( "Success Logout, cookie delete", HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>( "Sorry fail cookie delete", HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

}

package com.example.auctionuser.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;

@RequiredArgsConstructor
@Log4j2
@Service
public class JWTCookieService {

    @Value("${myToken.cookieJWTName}")
    private String JWT_COOKIE_NAME;

    @Value("${myToken.refreshJWTCookieName}")
    private String REFRESH_COOKIE_NAME;

    @Value("${myToken.userId}")
    private String REFRESH_COOKIE_ID;

    @Value("${myToken.cookieVerifyTime}")
    private int cookieVerifyTime;

    @Value("${myToken.cookieRefreshTime}")
    private int cookieRefreshTime;

    @Value("${myToken.cookieSecure}")
    private boolean cookieSecure;

    public Cookie addTokenToCookie(HttpServletResponse response, String token, String cookieName) {
        Cookie cookie = new Cookie(cookieName, token);
        log.info("add Cookie : "+ cookieName);


        if(cookieName.equals(JWT_COOKIE_NAME)){
            cookie.setMaxAge(cookieVerifyTime);
        }

        if(cookieName.equals(REFRESH_COOKIE_NAME)){
            cookie.setMaxAge(cookieRefreshTime);
        }


        cookie.setSecure(cookieSecure);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public Cookie addTokenToCookie(HttpServletResponse response, int token, String cookieName) {
        Cookie cookie = new Cookie(cookieName, String.valueOf(token));
        log.info("add Cookie : "+ cookieName);

        if(cookieName.equals(JWT_COOKIE_NAME)){
            cookie.setMaxAge(cookieVerifyTime);
        }

        if(cookieName.equals(REFRESH_COOKIE_NAME)){
            cookie.setMaxAge(cookieRefreshTime);
        }

        if(cookieName.equals(REFRESH_COOKIE_ID)){
            cookie.setMaxAge(cookieRefreshTime);
        }


        cookie.setSecure(cookieSecure);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public ArrayList<Cookie> logoutCookieDelete(Cookie[] cookies, HttpServletResponse response) {

        ArrayList<Cookie> returnCookies = new ArrayList<>();

        for (Cookie cookie : cookies
             ) {
            if(cookie.getName().equals(JWT_COOKIE_NAME) || cookie.getName().equals(REFRESH_COOKIE_NAME)
            || cookie.getName().equals(REFRESH_COOKIE_ID)){
                cookie.setMaxAge(-1);
                returnCookies.add(cookie);
            }
        }


        return returnCookies;
    }


}

package com.example.auctionuser.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.auctionuser.config.auth.PrincipalDetails;
import com.example.auctionuser.jwtutil.UserJWTUtil;

import com.example.auctionuser.model.UserModel;
import com.example.auctionuser.repository.UserModelRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Log4j2
public class JWTCheckFilter extends BasicAuthenticationFilter {
    private final UserJWTUtil userJwtUtil;

    private final UserModelRepository userModelRepository;

    private final String JWT_COOKIE_NAME;

    private final String REFRESH_COOKIE_NAME;

    private String REFRESH_COOKIE_ID;


    public JWTCheckFilter(AuthenticationManager authenticationManager,
                          UserJWTUtil loginFilterUserJWTUtil,
                          UserModelRepository userModelRepository,
                          Environment env){
        super(authenticationManager);
        this.userJwtUtil = loginFilterUserJWTUtil;
        this.userModelRepository = userModelRepository;
        JWT_COOKIE_NAME = env.getProperty("myToken.cookieJWTName");
        REFRESH_COOKIE_NAME = env.getProperty("myToken.refreshJWTCookieName");
        REFRESH_COOKIE_ID = env.getProperty("myToken.userId");
    }



    // 실직적인 필터가 하는쪽
    // 인증이나 권한이 필요한 요청이 있을때 해당필터를 타게된다
    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 응답 두번방지를 위해서 ,안그럼 그냥 넘겨버림
        //super.doFilterInternal(request, response, chain);

        log.info("UserService JWTCheckFilter has been activated.");

        String jwtHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        String reFreshJwtHeader = request.getHeader("RefreshToken");

        Cookie[] cookies = request.getCookies();

        String cookieJWT = null;

        String refreshCookieJWT = null;

        Integer cookieUserId = null;

        try{

            if(cookies != null){
                for (Cookie cookie: cookies
                ) {
                    System.out.println(cookie.getName() +" : "+ cookie.getValue());

                    if(cookie.getName().equals(JWT_COOKIE_NAME)){
                        cookieJWT = cookie.getValue();
                    }else if(cookie.getName().equals(REFRESH_COOKIE_NAME)){
                        refreshCookieJWT = cookie.getValue();
                    } else if (cookie.getName().equals(REFRESH_COOKIE_ID)) {
                        cookieUserId = Integer.parseInt(cookie.getValue());
                    }
                }

            }

            log.info(jwtHeader);
            log.info(reFreshJwtHeader);
            //System.out.println("테스트 헤더값체크 : " + jwtHeader);
            // 늘 리프레시와 액세스를 동시에 받아야함
            if(cookieJWT == null || refreshCookieJWT == null){
                log.info("This request have not token");
                chain.doFilter(request, response);
            }else{

                // 어째서인지 인증과정이 빠져있는데 나중에 꼭 넣어놓기

                try {
                    int userId = JWT.decode(cookieJWT).getClaim("userId").asInt();

                    Optional<UserModel> userModel =  userModelRepository.findById(userId);

                    if(userModel.isPresent()){
                        PrincipalDetails principalDetails = new PrincipalDetails(userModel.get());
                        // 사용자 인증 , 강제로 객체생성 , 마지막 인자를 보면 꼭 권한을 알려줘야함
                        // Authentication 객체를 생성
                        Authentication authentication =
                                new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

                        // 세션 공간, 강제로 시큐리티 세션에 접근, Authentication 객체를 저장
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }


                }catch (NullPointerException e){
                    // 토큰,리프레시 토큰 검색시 결과값이 없을때 , 회원가입을 다시해야하거나
                    // 다양한 문제가 발생

                    // 즉 verfy 를 실패했을때
                    log.info("JWTCheckFilter username null");
                }

                // 정상적으로 서명이 완료되고 반환값이 있을 예정
                chain.doFilter(request,response);
            }

        }catch (Exception e){
            log.error(e);
        }


    }

}

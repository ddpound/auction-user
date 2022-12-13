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
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Log4j2
public class JWTCheckFilter extends BasicAuthenticationFilter {
    private final UserJWTUtil userJwtUtil;

    private final UserModelRepository userModelRepository;




    public JWTCheckFilter(AuthenticationManager authenticationManager,
                          UserJWTUtil loginFilterUserJWTUtil,
                          UserModelRepository userModelRepository){
        super(authenticationManager);
        this.userJwtUtil = loginFilterUserJWTUtil;
        this.userModelRepository = userModelRepository;
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

        log.info(jwtHeader);
        log.info(reFreshJwtHeader);
        //System.out.println("테스트 헤더값체크 : " + jwtHeader);
        // 늘 리프레시와 액세스를 동시에 받아야함
        if(jwtHeader == null || !jwtHeader.startsWith("Bearer ")
                || reFreshJwtHeader == null || !reFreshJwtHeader.startsWith("Bearer ")){
            log.info("This request have not token");
            chain.doFilter(request, response);
        }else{

            String token = jwtHeader.replace("Bearer ", "");

            try {

                String username = JWT.decode(token).getClaim("username").asString();

                UserModel userModel =  userModelRepository.findByUsername(username);


                PrincipalDetails principalDetails = new PrincipalDetails(userModel);
                    // 사용자 인증 , 강제로 객체생성 , 마지막 인자를 보면 꼭 권한을 알려줘야함
                    // Authentication 객체를 생성

                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

                    // 세션 공간, 강제로 시큐리티 세션에 접근, Authentication 객체를 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }catch (NullPointerException e){
                // 토큰,리프레시 토큰 검색시 결과값이 없을때 , 회원가입을 다시해야하거나
                // 다양한 문제가 발생

                // 즉 verfy 를 실패했을때
                log.info("JWTCheckFilter username null");
            }
            // 정상적으로 서명이 완료되고 반환값이 있을 예정

            chain.doFilter(request,response);
        }
    }

}

package com.example.auctionuser.filters;


import com.example.auctionuser.config.auth.PrincipalDetails;
import com.example.auctionuser.jwtutil.JWTUtil;
import com.example.modulecommon.model.UserModel;
import com.example.modulecommon.repository.UserModelRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * JWT을 이용한 로그인 필터
 * */
@Log4j2
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final UserModelRepository userModelRepository;

    private final JWTUtil jwtUtil;

    public JWTLoginFilter(AuthenticationManager authenticationManager,
                          JWTUtil jwtUtil,UserModelRepository userModelRepository){

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userModelRepository =userModelRepository;
        setFilterProcessesUrl("/login/**");
    }

    // 1. username, password를 받아서
    // 2. 정상인지 로그인시도
    // /login 요청을 하면 로그인 시도를 위해서 실행됨
    // 로그인 시도시 PrincipalDetailsService가 호출
    // loadUserByUsername 호출
    // 마지막으로 principalDetails를 세션에 담아주고 JWT 토큰을 만들어서 반환해준다
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        log.info("JWTLoginFilter has been activated.");
        String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);
        // 즉 토큰 요청일때
        if(headerAuth != null){

            try {

                GoogleIdToken.Payload googlepayload = jwtUtil.googleVerify(headerAuth.substring("Bearer ".length()));

                // 구글 토큰 구조 체크를 위해
                //System.out.println(loginFilterJWTUtil.simpleDecode(headerAuth.substring("Bearer ".length())).getClaims());

                // 어쳐피 아래 로그인에서 해줌
                //UserModel userModel = tokenJoinService.findByUsername(googlepayload.getEmail());

                String username = googlepayload.getEmail();
                // 프론트 앤드 .env에도 비밀번호 저장해두고 사용할 예정
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username , jwtUtil.getUserSecretKey());

                // principalDetails의 loadUserByUsername() 함수를 찾아 실행함
                // 이게 loadUserByUsername()이 올바르게 작동하고 리턴값이 있다면
                // 올바르게 로그인했다는 뜻
                log.info("logiun try....");
                log.info("test" + authenticationManager);

                // 여기 담겨야지 로그인 성공
//                Authentication authentication =
//                        authenticationManager.authenticate(authenticationToken);
//                log.info("auth " + authentication);
//                SecurityContextHolder.getContext().setAuthentication(authentication);


                // 담기는지 확인한것
                //PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();


                //log.info("principalDetails : " + principalDetails.getUserModel().getUsername());

                // 리턴을 올바르게 해주면 세션에 저장됨
                log.info("login success");


                UserModel userModel =  userModelRepository.findByUsername(username);


                PrincipalDetails principalDetails = new PrincipalDetails(userModel);
                // 사용자 인증 , 강제로 객체생성 , 마지막 인자를 보면 꼭 권한을 알려줘야함
                // Authentication 객체를 생성

                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

                // 세션 공간, 강제로 시큐리티 세션에 접근, Authentication 객체를 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);




                return authentication;


                // 보통 에러는 UserDeatilsService returned null 즉 원하는 유저 값이없어서
                // UserDeatailsService가 null값을 반환하도록 만들어놔서 발생하는 에러

            }
            catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            // 로그인 신호에 담긴 request를 받아온다
        }



        return null;
    }


    // attemptAuthentication 실행후 성공시
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        // 로그인 시도가 성공했고 인증이 완료 되었다는 뜻입니다.
        log.info("successfulAuthentication");
        //log.info("Validation completed, login successful.");


        // 이건 해주면안댐
        //super.successfulAuthentication(request, response, chain, authResult);

        // 이게 있어야 필터가 다음값으로 넘어가줌
        chain.doFilter(request,response);
    }

}

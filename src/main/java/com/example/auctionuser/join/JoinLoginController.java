package com.example.auctionuser.join;

import com.example.auctionuser.config.auth.PrincipalDetails;
import com.example.auctionuser.jwtutil.JWTUtil;
import com.example.auctionuser.service.JoinService;
import com.example.auctionuser.service.JwtSuperintendService;
import com.example.auctionuser.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Log4j2
@RequiredArgsConstructor
@RestController
public class JoinLoginController {

    private final UserService userService;

    private final JoinService joinService;

    private final JWTUtil jwtUtil;

    private final JwtSuperintendService jwtSuperintendService;

    @GetMapping (value = "login/token/google")
    public ResponseEntity loginTryGoogle(Authentication authentication , HttpServletResponse response) {

        try {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

            String makeMyToken = jwtUtil.makeAuthToken(principalDetails.getUserModel());
            String makeRefleshToken = jwtUtil.makeRfreshToken(principalDetails.getUserModel());


            //log.info("make Token : "+makeMyToken);
            //헤더에 담아 전송, 프론트에 전달
            response.addHeader("Authorization", "Bearer "+ makeMyToken);
            response.addHeader("RefreshToken","Bearer "+ makeRefleshToken);

            jwtSuperintendService.saveCheckTokenRepository(principalDetails.getUsername(),makeMyToken,makeRefleshToken);

            return new ResponseEntity<>(userService.findUserNameFrontUserModel(principalDetails.getUsername()) , HttpStatus.OK);
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
            return new ResponseEntity<>( "Your membership registration is complete.", HttpStatus.OK);
        }else {
            return new ResponseEntity<>( "sorry fail join", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}

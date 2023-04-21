package com.example.auctionuser.seller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.auctionuser.model.SellerCoupon;
import com.example.auctionuser.model.UserModel;
import com.example.auctionuser.repository.SellerCouponRepository;
import com.example.auctionuser.repository.UserModelRepository;
import com.example.modulecommon.jwtutil.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class SellerService {

    private final JWTUtil jwtUtil;

    private final UserModelRepository userModelRepository;

    private final SellerCouponRepository sellerCouponRepository;

    @Value("${myToken.cookieJWTName}")
    private String JWT_COOKIE_NAME;

    @Value("${myToken.refreshJWTCookieName}")
    private String REFRESH_COOKIE_NAME;

    @Value("${myToken.userId}")
    private String REFRESH_COOKIE_ID;

    @Transactional
    public int sellerRegister(HttpServletRequest request, String id, String code){
        Cookie[] cookies = request.getCookies();
        String token = null;

        if(cookies == null){
            return -2; // no cookie
        }

        for (Cookie cookie: cookies
             ) {
            if(cookie.getName().equals(JWT_COOKIE_NAME)){
                token = cookie.getValue();

            }
        }
        Optional<UserModel> userModel = userModelRepository.findById(JWT.decode(token).getClaim("userId").asInt());

        SellerCoupon findSellerCoupon = null;
        try {
            findSellerCoupon = sellerCouponRepository.findByIdAndCouponPassword(Integer.parseInt(id),code);
        }catch (NumberFormatException e){
            return -5; // id가 String이 아닐때
        }


        // 키값이 날라오지 않음
        if(id.equals("") && code.equals("")){
            return -3;
        }

        // 등록된쿠폰 아님
        if(findSellerCoupon ==null){
            return -1;
        }

        // 만약 이미 등록된 쿠폰 이라는 뜻
        if(findSellerCoupon.getUserModel() != null){
            return -2;
        }

        // 새쿠폰이라면 그럼 쿠폰 등록자를 해주자
        findSellerCoupon.setUserModel(userModel.get());
        userModel.get().setRoles("ROLE_USER,ROLE_SELLER");
        return 1;
    }


}

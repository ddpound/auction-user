package com.example.auctionuser.config.auth;

import com.example.modulecommon.model.UserModel;
import com.example.modulecommon.repository.UserModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserModelRepository userModelRepository;

    // 시큐리티 세션 = Authentication = UserDetails
    @Override
    public UserDetails loadUserByUsername(String username) {

        // 제대로 작동하는지 체크
        log.info("PrincipalDetailsServiceOnline");

        UserModel userModel = userModelRepository.findByUsername(username);

        // 작동은 하지만 여기서 따로 검증해줘야하는 로직을 짜야하는듯 싶습니다.
        try {
            if (userModel != null) {
                // 시큐리티 세션 = Authentication( UserDetails) 이렇게 담겨진다
                // 그다음에는
                // 시큐리티 세션 ( Authentication( UserDetails) ) 이렇게 또 담긴다
                // 원래 대로라면
                // 그리고 세션이 만들어지면서 로그인이 완료됩니다.
                // log.info("principaldetail password : " + userModel.getPassword());
                // 여기서 넘겨주고 다시 받아줘야 로그인 완료
                return new PrincipalDetails(userModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // null이라는 뜻은 해당유저가 아니라는뜻
        return null;
    }
}

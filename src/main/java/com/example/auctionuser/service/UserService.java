package com.example.auctionuser.service;

import com.example.auctionuser.model.UserModel;
import com.example.auctionuser.model.UserModelFront;
import com.example.auctionuser.repository.JwtSuperintendRepository;
import com.example.auctionuser.repository.UserModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Service
public class UserService {

    private final UserModelRepository userModelRepository;

    private final JwtSuperintendRepository jwtSuperintendRepository;

    @Transactional(readOnly = true)
    public UserModelFront findUserNameFrontUserModel(String userName){

        // 이메일을 통해서 찾아냄
        UserModel finduserModel = userModelRepository.findByUsername(userName);

        if(finduserModel != null){

            List<String> roleList =  finduserModel.getRoleList();
            // 만약 일반유저가 아니라 여러 유저일경우

            // 그냥 기본적일때 가장 첫번째 것만 가져옴
            String resultRole = roleList.get(0);

            // 1개이상의 권한이 있다면 일반유저 이상
            if(roleList.size() == 2){
                resultRole = roleList.get(1);
            }else if(roleList.size() == 3){
                resultRole = roleList.get(2);
            }

            return UserModelFront.builder()
                    .id(finduserModel.getUserId())
                    .userName(finduserModel.getUsername())
                    .role(resultRole.replace("ROLE_",""))
                    .nickName(finduserModel.getNickname())
                    .picture(finduserModel.getPicture())
                    .build();

        }

        return null;
    }

}

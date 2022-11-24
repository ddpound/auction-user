package com.example.auctionuser.service;

import com.example.auctionuser.model.UserModel;
import com.example.auctionuser.repository.UserModelRepository;

import com.example.modulecommon.frontModel.UserModelFront;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Log4j2
@Service
public class UserService {

    private final UserModelRepository userModelRepository;

    /**
     * @param userNameorId String 으로 들어올때는 true,int로 들어올때는 False를 넣어줌
     *
     * */
    @Transactional(readOnly = true)
    public UserModelFront findUserNameFrontUserModel(String userNameorId, boolean intStringBoolean){

        Optional<UserModel> finduserModel = null;

        if(intStringBoolean){
            finduserModel = Optional.ofNullable(userModelRepository.findByUsername(userNameorId));
        }else{
            finduserModel = userModelRepository.findById(Integer.parseInt(userNameorId));
        }

        // 이메일을 통해서 찾아냄


        if(finduserModel.isPresent()){

            List<String> roleList =  finduserModel.get().getRoleList();
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
                    .id(finduserModel.get().getUserId())
                    .userName(finduserModel.get().getUsername())
                    .role(resultRole.replace("ROLE_",""))
                    .nickName(finduserModel.get().getNickname())
                    .picture(finduserModel.get().getPicture())
                    .build();

        }

        return null;
    }

    @Transactional
    public int saveAddress(){


        return 1;
    }

}

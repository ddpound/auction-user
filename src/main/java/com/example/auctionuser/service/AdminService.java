package com.example.auctionuser.service;

import com.example.auctionuser.config.auth.PrincipalDetails;

import com.example.auctionuser.model.IntegrateBoardModel;
import com.example.auctionuser.model.SellerCoupon;
import com.example.auctionuser.model.UserModel;
import com.example.auctionuser.repository.IntegrateBoardRepository;
import com.example.auctionuser.repository.SellerCouponRepository;
import com.example.auctionuser.repository.UserModelRepository;
import com.example.modulecommon.allstatic.AllStaticStatus;
import com.example.modulecommon.enums.AdminBoardCategory;
import com.example.modulecommon.enums.AuthNames;
import com.example.modulecommon.frontModel.SellerCouponFront;
import com.example.modulecommon.makefile.MakeFile;

import com.example.modulecommon.token.ReturnTokenUsername;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Log4j2
@Service
public class AdminService {
    private final UserModelRepository userModelRepository;

    private final SellerCouponRepository sellerCouponRepository;

    private final IntegrateBoardRepository integrateBoardRepository;

    private final ReturnTokenUsername returnTokenUsername;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final MakeFile makeFile;

    @Value("${jangadmin.secreyKey}")
    private String adminPassword;


    //체크 필터에서 가져온 authentication 값을 디비에 넣고 비밀번호도 받아와야함
    @Transactional
    public int giveAdmin(Authentication authentication, Map<String, Object> password) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        //저장해둔 비밀번호가 같다면
        if (adminPassword.equals(password.get("password"))) {
            //  db에서 세팅을 바꿔주자

            // 영속성 , 더티체킹
            UserModel userModel = userModelRepository.findByUsername(principalDetails.getUsername());
            userModel.setRoles("ROLE_USER,ROLE_SELLER,ROLE_ADMIN");
            return 1;

        } else {

            return -1;
        }


    }


    // DB에 판매자 쿠폰 등록을 만들어주는 서비스
    @Transactional
    public int makeCoupon(String num) {

        int length = 15;
        boolean useLetters = true;
        boolean useNumbers = true;


        if (num == null) {
            num = "1";
        }

        int newNum = Integer.parseInt(num);

        List<SellerCoupon> listSeller = new ArrayList<>();

        for (int i = 0; i < newNum; i++) {
            String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);


            listSeller.add(SellerCoupon.builder().couponPassword(generatedString).build());
        }

        sellerCouponRepository.saveAll(listSeller);


        return 1;
    }

    // 삭제할 때 유저 권환도 취소해야함
    @Transactional
    public int deleteCoupon(int id) {

        // 영속화
        Optional<SellerCoupon> sellerCoupon = sellerCouponRepository.findById(id);

        if (sellerCoupon.get().getUserModel() != null) {
            // 더티체킹
            sellerCoupon.get().getUserModel().setRoles("ROLE_USER");
        }


        sellerCouponRepository.deleteById(id);
        return 1;
    }


    // 따로 담아서 줘야함
    @Transactional(readOnly = true)
    public List<SellerCouponFront> findAllCoupon() {

        List<SellerCoupon> resultCouponlist = sellerCouponRepository.findAll();

        ArrayList<SellerCouponFront> sellerCouponFrontsArray = new ArrayList<>();


        for (SellerCoupon list : resultCouponlist
        ) {
            if (list != null && list.getUserModel() != null) {
                sellerCouponFrontsArray.add(SellerCouponFront
                        .builder()
                        .id(list.getId())
                        .couponCode(list.getCouponPassword())
                        .userId(list.getUserModel().getUserId())
                        .userName(list.getUserModel().getUsername())
                        .build());
            } else if (list != null) {
                sellerCouponFrontsArray.add(SellerCouponFront
                        .builder()
                        .id(list.getId())
                        .couponCode(list.getCouponPassword())
                        .build());
            }

        }

        return sellerCouponFrontsArray;
    }

    // 유저의 아이디이자 폴더 이름 가져온다는 뜻
    @Transactional(readOnly = true)
    public String saveAnnouncementBoardImageFIle(int userAndBoardId, String content) {

        // 파일 관련 부분
        String returnFolderPath = makeFile.saveMoveImageFiles(userAndBoardId, content , AuthNames.Admin,"").get(2);


        makeFile.deleteTemporary(userAndBoardId);

        return returnFolderPath;

    }

    @Transactional
    public void saveAnnouncementBoard(IntegrateBoardModel boardModel,
                                      String folderNamePath,
                                      HttpServletRequest request,
                                      boolean modify,
                                      Integer boardId) {

        // 배포때는 수정해야할 듯
        String mainurl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/";

        Map<Integer, String> returnFileResult = null;

        // http://localhost:5000/Temporrary_files/1/ 까지의 파일경로를 변경해주자
        String changeTargetFolderPath
                = mainurl + AllStaticStatus.temporaryImageFiles.substring(
                AllStaticStatus.temporaryImageFiles
                        .indexOf("Temporary"))
                + boardModel.getUserModel().getUserId() + "/";

        // 앞의 C나 home 루트를 제외시킴
        String changeFolderPath = mainurl + folderNamePath
                .substring(AllStaticStatus.saveImageFileRoot.indexOf("Jang"));

        // 바꿔줘야함 문자열 받은걸
        String changeBoardContent = boardModel.getContent().replace(changeTargetFolderPath, changeFolderPath);
        boardModel.setContent(changeBoardContent);

        // 수정작업 들어감
        if(boardId != -2 && modify){
            // 영속화
            Optional<IntegrateBoardModel> integrateBoardModel =  integrateBoardRepository.findById(boardId);

            // 더티체킹
            integrateBoardModel.get().setContent(boardModel.getContent());
            integrateBoardModel.get().setTitle(boardModel.getTitle());
        }else{
            integrateBoardRepository.save(boardModel);
        }

    }

    /**
     * 삭제 메소드
     * 글쓰기에 사용된 파일도 삭제됨
     * */
    @Transactional
    public int deleteBoardById(int id, HttpServletRequest request){

        String jwtHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String jwtRHeader = request.getHeader("RefreshToken");

        String token = jwtHeader.replace("Bearer ", "");

        Map<Integer, Object> returnMapUserData = returnTokenUsername.tokenGetUsername(request);

        Optional<IntegrateBoardModel> findIntegrateBoardModel = integrateBoardRepository.findById(id);

        try{
            if(findIntegrateBoardModel.isPresent()){
                log.info("success delete admin board id : " + id);

                // 파일도 같이 삭제
                if(findIntegrateBoardModel.get().getFilefolderPath().length() >0){
                    makeFile.folderPathImageDelete(findIntegrateBoardModel.get().getFilefolderPath());
                }

                integrateBoardRepository.deleteById(id);

                return 1;
            }else{

                log.error("Not Found Delete Board");
                return -1;
            }
        }catch (Exception e){
            log.error(e);
            return -1;
        }

    }

    // 카테고리별로 가져올수 있는 통합 메소드
    @Transactional(readOnly = true)
    public List<IntegrateBoardModel> findAllAndCategoryBoard(AdminBoardCategory adminBoardCategory) {

        ArrayList<IntegrateBoardModel> arrayList = new ArrayList<>();

        for (IntegrateBoardModel intergrateBoard : integrateBoardRepository.findAllByAdminBoardCategory(adminBoardCategory)
        ) {
            intergrateBoard.setUserModel(null);
            arrayList.add(intergrateBoard);
        }

        return arrayList;
    }

    @Transactional(readOnly = true)
    public Optional<IntegrateBoardModel> findAnnouncementBoard(int boardId) {

        Optional<IntegrateBoardModel> integrateBoardModel = integrateBoardRepository.findById(boardId);
        integrateBoardModel.ifPresent(integrateBoardModel1 -> integrateBoardModel.get().setUserModel(null));

        return integrateBoardModel;
    }



}

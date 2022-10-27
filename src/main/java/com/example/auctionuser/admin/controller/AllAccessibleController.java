package com.example.auctionuser.admin.controller;

import com.example.auctionuser.model.IntegrateBoardModel;
import com.example.auctionuser.service.AdminService;
import com.example.modulecommon.enums.AdminBoardCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * 모두가 접근가능한 컨트롤러
 * */
@Log4j2
@RequiredArgsConstructor
@RequestMapping("auth")
@RestController
public class AllAccessibleController {


    private final AdminService adminService;

    @GetMapping(value = "find-announcement-board/{boardId}")
    public ResponseEntity<Optional<IntegrateBoardModel>> findAnnuncementBoard(
            HttpServletRequest request,
            @PathVariable int boardId
    ){
        return new ResponseEntity<>(adminService.findAnnouncementBoard(boardId), HttpStatus.OK);
    }

    @GetMapping(value = "find-all-announcement-board")
    public ResponseEntity<List<IntegrateBoardModel>> findAllAnnouncementBoard(
            HttpServletRequest request
    ){

        return new ResponseEntity<>(adminService.findAllAndCategoryBoard(AdminBoardCategory.Announcemnet), HttpStatus.OK);
    }

}

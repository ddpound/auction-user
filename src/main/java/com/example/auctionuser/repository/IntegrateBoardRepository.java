package com.example.auctionuser.repository;

import com.example.auctionuser.model.IntegrateBoardModel;
import com.example.modulecommon.enums.AdminBoardCategory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IntegrateBoardRepository extends JpaRepository<IntegrateBoardModel,Integer> {


    List<IntegrateBoardModel> findAllByAdminBoardCategory(AdminBoardCategory adminBoardCategory);

    void deleteById(int id);

}
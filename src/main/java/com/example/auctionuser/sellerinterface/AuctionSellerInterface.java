package com.example.auctionuser.sellerinterface;

import com.example.auctionuser.model.dto.ReservationDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@FeignClient(name = "auction-seller")
public interface AuctionSellerInterface {

    @RequestMapping(method = RequestMethod.POST, value = "/seller/save-reply", consumes = "application/json")
    ResponseEntity<String> saveReply(@RequestHeader("Authorization")String token,
                                     @RequestHeader("RefreshToken") String reToken,
                                     @RequestParam(value="content", required=false)String content,
                                     @RequestParam(value="userId", required=false)int userId,
                                     @RequestParam(value="nickName", required=false)String nickName,
                                     @RequestParam(value="userPicture", required=false)String userPicture,
                                     @RequestParam(value="boardId", required=false)int boardId);

    @RequestMapping(method = RequestMethod.POST, value = "/seller/save-reply/of-reply", consumes = "application/json")
    ResponseEntity<String> saveReplyOfReply(@RequestHeader("Authorization")String token,
                                            @RequestHeader("RefreshToken") String reToken,
                                            @RequestParam(value="content", required=false)String content,
                                            @RequestParam(value="userId", required=false)int userId,
                                            @RequestParam(value="nickName", required=false)String nickName,
                                            @RequestParam(value="userPicture", required=false)String userPicture,
                                            @RequestParam(value="boardId", required=false)int boardId,
                                            @RequestParam(value="replyId", required=false)int replyId);

    @RequestMapping(method = RequestMethod.DELETE, value = "/seller/delete-reply/{id}")
    ResponseEntity<String> deleteReply(@RequestHeader("Authorization")String token,
                                       @RequestHeader("RefreshToken") String reToken,
                                       @PathVariable(value = "id") Integer id);

    @RequestMapping(method = RequestMethod.DELETE, value = "/seller/delete-reply/of-reply/{id}")
    ResponseEntity<String> deleteReplyOfReply(@RequestHeader("Authorization")String token,
                                              @RequestHeader("RefreshToken") String reToken,
                                              @PathVariable(value = "id") Integer id);

    @RequestMapping(method = RequestMethod.POST, value = "/seller/save-reservation", consumes = "application/json")
    ResponseEntity<String> saveReservation(@RequestHeader("Authorization")String token,
                                           @RequestHeader("RefreshToken") String reToken,
                                           @RequestBody ReservationDetails reservationDetails);
}

package com.example.auctionuser.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReservationDetails {

    private int id;

    // 제품 아이디
    private int productId;

    private int quantity;

    private int shoppingMallId;

    // 구매자 아이디
    private int buyerId;

    private String buyerNickName;

    private String address;

    private List<OptionDto> optionList;


}

package com.example.auctionuser.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OptionDto {

    private int id;

    private String detailedDescription;

    private String optionTitle;

    private int productId;
}

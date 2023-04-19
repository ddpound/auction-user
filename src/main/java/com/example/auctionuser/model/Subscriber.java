package com.example.auctionuser.model;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int shoppingMallId;

    private String shoppingMallName;

    @ManyToOne
    private UserModel user;


}
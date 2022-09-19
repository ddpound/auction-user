package com.example.auctionuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example.auctionuser","com.example.modulecommon"})
public class AuctionUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuctionUserApplication.class, args);
    }

}

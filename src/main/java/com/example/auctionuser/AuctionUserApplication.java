package com.example.auctionuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.example.auctionuser","com.example.modulecommon"})
@EnableFeignClients
public class AuctionUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuctionUserApplication.class, args);
    }

}

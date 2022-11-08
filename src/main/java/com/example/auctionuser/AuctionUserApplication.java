package com.example.auctionuser;

import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"com.example.auctionuser","com.example.modulecommon"})
@EnableFeignClients
public class AuctionUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuctionUserApplication.class, args);
    }

    @Bean
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

}

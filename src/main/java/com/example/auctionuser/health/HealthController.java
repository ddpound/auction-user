package com.example.auctionuser.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "auction-user")
public class HealthController {

    @GetMapping(value = "/")
    public String getHello(){
        return "Hello World";
    }

    @GetMapping(value = "health")
    public String getHealth(){
        return "Service is good";
    }

}

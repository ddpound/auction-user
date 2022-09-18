package com.example.auctionuser.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "health")
public class HealthController {

    @GetMapping(value = "hello")
    public String getHello(){
        return "Hello World";
    }

    @GetMapping(value = "service-check")
    public String getHealth(){
        return "Service is good";
    }

}

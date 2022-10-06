package com.example.auctionuser.health;

import lombok.RequiredArgsConstructor;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "health")
public class HealthController {

    private final Environment env;

    @GetMapping(value = "hello")
    public String getHello(){
        return "Hello World";
    }

    @GetMapping(value = "service-check")
    public String getHealth(){
        return "Service is good";
    }

    @GetMapping(value = "service-yml")
    public String getYml(){
        return env.getProperty("commonTestText");
    }

}

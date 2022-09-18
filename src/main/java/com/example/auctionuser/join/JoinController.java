package com.example.auctionuser.join;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("join")
public class JoinController {

    @GetMapping("googletoken")
    public String googleTokenJoin(){

        return "test";
    }
}

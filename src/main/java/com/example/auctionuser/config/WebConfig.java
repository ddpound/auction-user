package com.example.auctionuser.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")      //패턴
                .allowedOriginPatterns("http://localhost:3000") //URL
                .allowedHeaders("Authorization","RefreshToken")  //header
                .allowCredentials(true)
                .allowedMethods("GET","POST");        //method
    }
}
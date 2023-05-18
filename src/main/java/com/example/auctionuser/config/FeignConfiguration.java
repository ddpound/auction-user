package com.example.auctionuser.config;

import feign.RequestInterceptor;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {

    @Bean
    public RequestInterceptor feignCookieInterceptor(CookieStore cookieStore) {
        return new FeignCookieInterceptor(cookieStore);
    }

    @Bean
    public CookieStore cookieStore() {
        return new BasicCookieStore();
    }
}

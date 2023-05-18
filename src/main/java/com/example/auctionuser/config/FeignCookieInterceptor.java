package com.example.auctionuser.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.http.client.CookieStore;
import org.springframework.context.annotation.Bean;


import java.util.stream.Collectors;

public class FeignCookieInterceptor implements RequestInterceptor {

    private final CookieStore cookieStore;

    public FeignCookieInterceptor(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }

    @Override
    public void apply(RequestTemplate template) {
        if (cookieStore != null && !cookieStore.getCookies().isEmpty()) {
            String cookies = cookieStore.getCookies().stream()
                    .map(cookie -> cookie.getName() + "=" + cookie.getValue())
                    .collect(Collectors.joining("; "));
            template.header("Cookie", cookies);
        }
    }

}

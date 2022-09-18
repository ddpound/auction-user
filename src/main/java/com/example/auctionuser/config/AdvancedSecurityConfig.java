package com.example.auctionuser.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;



@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor // 요즘 autowierd 대신쓰기위해 나온것
@Configuration
public class AdvancedSecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // session은 안하는걸로 , csrf 끄기
        http
                .authorizeRequests()
                .antMatchers("/join/**").permitAll()
                .anyRequest().authenticated()
                .and()
                // cors config 클래스로 설정을 줄꺼여서 그냥 이대로 주석처리
                // 유저 패스워드 값으로 로그인을 진행 안함 , 폼로그인 x
                .formLogin().disable()
                //.cors().disable()
                .csrf().disable()
                //.addFilter(corsFilter) // @CrossOrigin (인증 x), 시큐리티 필터 등록 인증
                // 기본적인 http 로그인방식도 사용하지않는다.
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        return http.build();
    }
}

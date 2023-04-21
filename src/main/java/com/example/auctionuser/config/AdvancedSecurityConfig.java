package com.example.auctionuser.config;

import com.example.auctionuser.filters.JWTCheckFilter;
import com.example.auctionuser.filters.JWTLoginFilter;
import com.example.auctionuser.jwtutil.UserJWTUtil;
import com.example.auctionuser.repository.UserModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor // 요즘 autowierd 대신쓰기위해 나온것
@Configuration
public class AdvancedSecurityConfig {

    private final UserModelRepository userModelRepository;

    @Value("${securityIpAddress.AuthorizedAddress}")
    private String authorizedAddress;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
    /**
     * 필터에서도 써줘야하니 여기서 미리 빈 등록을 해주자
     * */
    @Bean
    public UserJWTUtil jwtUtil() {return new UserJWTUtil();}

    public final Environment environment;

    private final CorsFilter corsFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http , AuthenticationManager authenticationManager) throws Exception {
        http
                .csrf().disable()
                // session은 안하는걸로 , csrf 끄기
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // cors config 클래스로 설정을 줄꺼여서 그냥 이대로 주석처리
                // 유저 패스워드 값으로 로그인을 진행 안함 , 폼로그인 x
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .addFilter(corsFilter) // @CrossOrigin (인증 x), 시큐리티 필터 등록 인증
                // 기본적인 http 로그인방식도 사용하지않는다.
                .httpBasic().disable()
                .formLogin().disable()
                .addFilter(new JWTLoginFilter(authenticationManager, jwtUtil(),userModelRepository))
                .addFilter(new JWTCheckFilter(authenticationManager,
                        jwtUtil(),
                        userModelRepository,
                        environment))
                .authorizeRequests()
                //.antMatchers("/join/**", "/login/**", "/health/**", "/actuator/**", "/auth/**").permitAll()
                .antMatchers("/user/**").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_SELLER')")
                .antMatchers("/seller/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/error/**").authenticated()
                .anyRequest().hasIpAddress(authorizedAddress);


        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(Arrays.asList("http://localhost:3000"));
        config.setAllowedMethods(Arrays.asList("HEAD","POST","GET","DELETE","PUT"));
        config.setAllowCredentials(true);
        config.setExposedHeaders(Arrays.asList("Authorization","RefreshToken"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}

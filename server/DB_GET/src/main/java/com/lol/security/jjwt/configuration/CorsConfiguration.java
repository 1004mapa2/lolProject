package com.lol.security.jjwt.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfiguration {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();
        config.setAllowCredentials(true); //내 서버가 응답을 할 때 json 을 javascript 에서 처리할 수 있게 할 지를 설정
        config.addExposedHeader("Authorization"); //헤더 참조할 수 있게 설정
        config.addAllowedOrigin("http://3.37.36.48:3000");
        config.addAllowedHeader("*"); //모든 header에 응답을 허용
        config.addAllowedMethod("*"); //모든 post, get, put, delete, patch 요청을 허용
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}

package com.lol.security.jjwt.configuration;

import com.lol.repository.LoginMapper;
import com.lol.repository.RedisRepository;
import com.lol.security.jjwt.auth.JwtTokenizer;
import com.lol.security.jjwt.filter.JwtAuthenticationFilter;
import com.lol.security.jjwt.filter.JwtAuthorizationFilter;
import com.lol.security.jjwt.handler.JwtAccessDeniedHandler;
import com.lol.security.jjwt.handler.JwtAuthenticationEntryPoint;
import com.lol.security.jjwt.handler.JwtAuthenticationFailureHandler;
import com.lol.security.jjwt.handler.JwtAuthenticationSuccessHandler;
import com.lol.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtTokenizer jwtTokenizer;
    private final CorsFilter corsFilter;
    private final LoginMapper mapper;
    private final RedisRepository redisRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .accessDeniedHandler(new JwtAccessDeniedHandler())
                .and()
                .addFilter(corsFilter)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .apply(new CustomFilterConfigurer())
                .and()
                .authorizeRequests(authorize -> authorize
                        .antMatchers("/api/admin/**").hasRole("ADMIN")
                        .antMatchers("/api/manager/**").hasAnyRole("ADMIN", "MANAGER")
                        .antMatchers("/api/user/**").hasAnyRole("ADMIN", "MANAGER", "USER")
                        .antMatchers("/api/init").hasAnyRole("ADMIN", "MANAGER", "USER")
                        .antMatchers("/api/refresh").hasAnyRole("ADMIN", "MANAGER", "USER")
                        .antMatchers("/saveComment").hasAnyRole("ADMIN", "MANAGER", "USER")
                        .antMatchers("/board/postBoard").hasAnyRole("ADMIN", "MANAGER", "USER")
                        .antMatchers("/board/postComment").hasAnyRole("ADMIN", "MANAGER", "USER")
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtTokenizer, redisRepository);
            jwtAuthenticationFilter.setFilterProcessesUrl("/api/login"); // 디폴트 url "/login"을 원하는 url 로 변경
            jwtAuthenticationFilter.setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler());
            jwtAuthenticationFilter.setAuthenticationFailureHandler(new JwtAuthenticationFailureHandler());
            JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(jwtTokenizer, mapper, redisRepository);

            builder.addFilter(jwtAuthenticationFilter)
                    .addFilterAfter(jwtAuthorizationFilter, JwtAuthenticationFilter.class);
        }
    }
}

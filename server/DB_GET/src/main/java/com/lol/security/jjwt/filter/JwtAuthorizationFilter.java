package com.lol.security.jjwt.filter;


import com.lol.dto.Token;
import com.lol.dto.UserDto;
import com.lol.repository.LoginMapper;
import com.lol.repository.RedisRepository;
import com.lol.security.jjwt.auth.CustomUserDetails;
import com.lol.security.jjwt.auth.JwtTokenizer;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenizer jwtTokenizer;
    private final LoginMapper mapper;
    private final RedisRepository redisRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (findAccessToken(request)) {
                Map<String, Object> claims = verifyJws(request);
                setAuthenticationToContext(claims);
            }
        } catch (ExpiredJwtException e) {
            /**
             * 1. Refresh 가져오기
             * 2. Redis 에 Refresh 가 있는지 확인 후 비교
             * 3. 있고 같다면 AccessToken 생성
             * 4. response.Header 에 넣기
             * 5. Redis 에서 그 RefreshToken 에 매칭되는 AccessToken 업데이트
             */
            //refresh 가 없는 경우 구현해야됨.
            String refresh = "";
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if ("Refresh".equals(cookie.getName())) {
                    refresh = cookie.getValue();
                }
            }
            String jws = request.getHeader("Authorization");
            Optional<Token> optionalToken = redisRepository.findByAccessToken(jws);
            if (optionalToken.get().getRefreshToken().equals(refresh)) {
                String username = (String) e.getClaims().get("username");
                Map<String, Object> claims = new HashMap<>();
                claims.put("username", username);
                Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
                String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
                String accessToken = jwtTokenizer.generateAccessToken(claims, username, expiration, base64EncodedSecretKey);

                response.setHeader("Authorization", "Bearer " + accessToken);
                Token token = redisRepository.findById(refresh).orElse(null);
                token.setAccessToken("Bearer " + accessToken);
                redisRepository.save(token);

                setAuthenticationToContext(claims);
            }
        } catch (JwtException e) {
            request.setAttribute("exception", e);
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthenticationToContext(Map<String, Object> claims) {
        String username = (String) claims.get("username");
        Optional<UserDto> optionalUserDto = mapper.findByUser(username);
        UserDto userDto = optionalUserDto.orElseThrow(() -> new NullPointerException());
        CustomUserDetails customUserDetails = new CustomUserDetails(userDto);
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String authorization = request.getHeader("Authorization");

        return authorization == null || !authorization.startsWith("Bearer");
    }

    private Map<String, Object> verifyJws(HttpServletRequest request) {
        String jws = request.getHeader("Authorization").replace("Bearer ", "");
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        Map<String, Object> claims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();

        return claims;
    }

    private Boolean findAccessToken(HttpServletRequest request) {
        String jws = request.getHeader("Authorization");
        Optional<Token> optionalToken = redisRepository.findByAccessToken(jws);
        if (optionalToken.isPresent()) {
            return true;
        } else {
            return false;
        }
    }
}
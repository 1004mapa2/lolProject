package com.lol.security.jjwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lol.dto.Token;
import com.lol.dto.user.UserAccount;
import com.lol.repository.RedisRepository;
import com.lol.security.jjwt.auth.JwtTokenizer;
import com.lol.security.jjwt.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenizer jwtTokenizer;
    private final RedisRepository redisRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            UserAccount userAccount = objectMapper.readValue(request.getInputStream(), UserAccount.class);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userAccount.getUsername(), userAccount.getPassword());

            //PrincipalDetailsService 의 loadUserByUsername() 함수가 실행된다.
            //authentication 에 정보가 있다면 DB에 있는 username 과 password 가 일치한다는 뜻
            //authentication 객체가 session 영역에 저장을 해야하고 그 방법이 return
            //굳이 JWT 토큰을 사용하면서 세션을 만들 이유가 없지만 권한 처리 때문에 session 에 넣어준다.
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //attemptAuthentication 실행 후 인증이 정상적으로 됐으면 successfulAuthentication 함수가 실행된다.
    //여기서 JWT 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response 해주면 된다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

        String accessToken = createAccessToken(customUserDetails);
        String refreshToken = createRefreshToken(customUserDetails);

        response.setHeader("Authorization", "Bearer " + accessToken);
        Cookie cookie = new Cookie("Refresh", refreshToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(jwtTokenizer.getRefreshTokenExpirationMinutes());
        response.addCookie(cookie);

        Token token = new Token();
        token.setAccessToken("Bearer " + accessToken);
        token.setRefreshToken(refreshToken);
        token.setExpiration((long) jwtTokenizer.getRefreshTokenExpirationMinutes());
        redisRepository.save(token);

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }

    protected String createAccessToken(CustomUserDetails customUserDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", customUserDetails.getUsername());

        String subject = customUserDetails.getUsername();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        return accessToken;
    }

    private String createRefreshToken(CustomUserDetails customUserDetails) {
        String subject = customUserDetails.getUsername();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);

        return refreshToken;
    }
}

package com.hufs.AnswerDev.Util;

import com.hufs.AnswerDev.Model.User.CustomUserDetails;
import com.hufs.AnswerDev.Model.User.UserService;
import com.hufs.AnswerDev.Util.Enum.JwtEnum;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.accessExpiration}")
    private long accessExpiration;
    @Value("${jwt.refreshExpiration}")
    private long refreshExpiration;
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Lazy
    @Autowired
    UserService userService;

    @Async
    public Map<String, Integer> createClaim(int userId) {
        Map<String, Integer> claim = new HashMap<>();
        claim.put("userId", userId);
        return claim;
    }

    @Async
    public Key getSignKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8); // 암호화 키를 바이트 배열로 분해
        return Keys.hmacShaKeyFor(keyBytes); // HMAC-SHA512 알고리즘에 사용할 키 생성
    }

    @Async
    public JwtParser getJwtParser() {
        Key key = getSignKey();
        return Jwts.parser().verifyWith((SecretKey) key).build();
    }

    @Async
    public CompletableFuture<String> createToken(int userId, JwtEnum tokenType) {
        Map<String, Integer> claim = createClaim(userId);

        Date now = new Date();
        Date expireTime;
        switch (tokenType) {
            case ACCESSTOKEN:
                expireTime = new Date(now.getTime() + accessExpiration);
                break;
            default:
                expireTime = new Date(now.getTime() + refreshExpiration);
                break;
        }

        Key key = getSignKey();
        String token = Jwts.builder()
                .setClaims(claim)
                .setIssuedAt(now)
                .setExpiration(expireTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return CompletableFuture.completedFuture(token);
    }

    public int validateToken(String token) {
        try {
            JwtParser jwtParser = getJwtParser();

            return (int) jwtParser.parseSignedClaims(token).getPayload().get("userId"); // 토큰 검증 후 userId 반환
        } catch (MalformedJwtException exception) {
            throw new ResponseStatusException(HttpStatus.valueOf(401), exception.getMessage());
        }
    }

    @Async
    public CompletableFuture<Authentication> getAuthentication(int userId) {
        CustomUserDetails customUserDetails = new CustomUserDetails(this.userService.findById(userId));
        return CompletableFuture.completedFuture(new UsernamePasswordAuthenticationToken(customUserDetails, "", customUserDetails.getAuthorities()));
    }
}

package com.hufs.AnswerDev.Util;

import com.hufs.AnswerDev.Util.Enum.JwtEnum;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.accessExpiration}")
    private long accessExpiration;
    @Value("${jwt.refreshExpiration}")
    private long refreshExpiration;
    @Value("${jwt.secretKey}")
    private String secretKey;

    public static Map<String, Integer> createClaim(int userId) {
        Map<String, Integer> claim = new HashMap<>();
        claim.put("userId", userId);
        return claim;
    }

    public Key getSignKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8); // 암호화 키를 바이트 배열로 분해
        return Keys.hmacShaKeyFor(keyBytes); // HMAC-SHA512 알고리즘에 사용할 키 생성
    }

    public String createToken(int userId, JwtEnum tokenType) {
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

        return token;
    }
}

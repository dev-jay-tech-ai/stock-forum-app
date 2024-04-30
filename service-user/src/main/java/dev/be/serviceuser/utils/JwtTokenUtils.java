package dev.be.serviceuser.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtTokenUtils {
    private static String secretKey;
    @Value("${jwt.secret-key}")
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public static Boolean validate(String token, String userName, String key) {
        String usernameByToken = getUsername(token, key);
        return usernameByToken.equals(userName) && !isTokenExpired(token, key);
    }

    public static Claims extractAllClaims(String token, String key) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(key))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String getUsername(String token, String key) {
        return extractAllClaims(token, key).get("username", String.class);
    }

    private static Key getSigningKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static Boolean isTokenExpired(String token, String key) {
        Date expiration = extractAllClaims(token, key).getExpiration();
        return expiration.before(new Date());
    }

    public static String generateAccessToken(String username, String key, long expiredTimeMs) {
        return doGenerateToken(username, expiredTimeMs, key);
    }

    private static String doGenerateToken(String username, long expireTime, String key) {
        Claims claims = Jwts.claims();
        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSigningKey(key), SignatureAlgorithm.HS256)
                .compact();
    }

    //JWT 토큰의 만료시간
    public static Long getExpiration(String accessToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();

            Date expiration = claims.getExpiration();
            if (expiration == null) {
                throw new RuntimeException("Token expiration is not defined");
            }

            long now = System.currentTimeMillis();
            long expirationTime = expiration.getTime();
            if (expirationTime <= now) {
                return 0L; // Token already expired
            }

            return expirationTime - now;
        } catch (JwtException e) {
            throw new RuntimeException("Invalid token: " + e.getMessage());
        }
    }
}

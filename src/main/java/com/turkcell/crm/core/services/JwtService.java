package com.turkcell.crm.core.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class JwtService {
    @Value("${jwt.secret.key}")
    private String secretKey;
    @Value("${jwt.expiration.ms}")
    private long expiration;

    public String generateToken(String userName, Map<String, Object> claims) {
        return createToken(userName, claims);
    }

    private String createToken(String userName, Map<String, Object> claims) {
        return Jwts.builder()
                .claims(claims)
                .subject(userName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey())
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Boolean validateToken(String token) {
        Date expirationDate = getClaims(token).getExpiration();
        return expirationDate.after(new Date());
    }

    public List<String> extractRoles(String token) {
        return getClaims(token).get("roles", List.class);
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String randomRefreshToken() {
        byte[] numberByte = new byte[33];
        SecureRandom random = new SecureRandom();
        random.nextBytes(numberByte);
        return Base64.getEncoder().encodeToString(numberByte);
    }
}

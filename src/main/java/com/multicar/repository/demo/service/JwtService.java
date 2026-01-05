package com.multicar.repository.demo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret:your-secret-key-change-this-in-production-use-strong-random-key}")
    private String secretKey;

    @Value("${jwt.expiration-hours:24}")
    private Long expirationHours;

    private SecretKey getSigningKey() {
        // First try to get from environment variable, fallback to properties
        String key = System.getenv("JWT_SECRET");
        if (key == null || key.isEmpty()) {
            key = secretKey;
        }
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

    private Date getExpirationDate() {
        long expirationMillis = expirationHours * 60 * 60 * 1000;
        return new Date(System.currentTimeMillis() + expirationMillis);
    }

    public String generateToken(String userId, String emailId, String userType, String companyId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("emailId", emailId);
        claims.put("userType", userType);
        if (companyId != null) {
            claims.put("companyId", companyId);
        }
        return createToken(claims, emailId);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(getExpirationDate())
                .signWith(getSigningKey())
                .compact();
    }

    public String extractEmailId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", String.class));
    }

    public String extractUserType(String token) {
        return extractClaim(token, claims -> claims.get("userType", String.class));
    }

    public String extractCompanyId(String token) {
        return extractClaim(token, claims -> {
            Object companyId = claims.get("companyId");
            return companyId != null ? companyId.toString() : null;
        });
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}







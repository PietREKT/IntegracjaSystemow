package com.example.IntegracjaSystemow.configs;

import com.example.IntegracjaSystemow.users.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;

@Component
@Log4j2
public class JWTService {
    @Value("${jwt.key}")
    String key;

    @Value("${jwt.expiration}")
    Integer expireAfter;

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(key.getBytes());
    }

    public String generateToken(User user){
        HashMap<String, Object> claims = new HashMap<>();

        return generateToken(claims, user);
    }

    private String generateToken(HashMap<String, Object> claims, User user){
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (expireAfter * 60 * 1000)))
                .claims()
                .add(claims)
                .and()
                .signWith(getSigningKey())
                .compact();
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsername(String token){
        return extractAllClaims(token).getSubject();
    }
}

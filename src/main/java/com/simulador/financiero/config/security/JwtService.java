package com.simulador.financiero.config.security;

import java.sql.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.simulador.financiero.entities.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    public String generateToken(UserEntity user){
        return buildToken(user, expirationTime);
    }

    private String buildToken(UserEntity user, final long expirationTime){
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("id", user.getId())
                .claim("fullName", user.getFullName())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignInKey())
                .compact();
    }

    public boolean isTokenValid(String token){
        boolean isValid = true;

        try{
            Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        }catch(Exception e){
            isValid = false;
        }
        return isValid;
    }

    public String getUsernameFromToken(String token){
        return getClaim(token, Claims:: getSubject );
    }

    public <T> T getClaim(String token, Function<Claims, T> claimGetterFunction){
        Claims claims = extractAllClaims(token);
        return claimGetterFunction.apply(claims);
    }

    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        
    }


    public long getExpirationTime() {
        return expirationTime / 1000;
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

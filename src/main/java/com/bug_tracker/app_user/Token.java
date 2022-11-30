package com.bug_tracker.app_user;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

public class Token {
    @Getter
    private final String token;

    private  Token(String token){
        this.token = token;
    }

    public static Token of(Long userId, Long validityInMinutes, String secretKey){
        var issueDate = Instant.now();
        return new Token( Jwts.builder()
                .claim("user_id",userId)
                .setIssuedAt(Date.from(issueDate))
                .setExpiration(Date.from(issueDate.plus(validityInMinutes, ChronoUnit.MINUTES)))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                //.signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8)))
                .compact());
    }

    public static Token of(String token){
        return new Token(token);
    }



    public static Long from(final String token,final String secretKey){


        Jws<Claims> jws;
        try {
        jws = Jwts.parserBuilder().
                setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build().parseClaimsJws(token);

        } catch (JwtException ex) {
            System.out.println("adw");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());

        }

        return jws.getBody().get("user_id", Long.class);
    }

}

package com.bug_tracker.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;


public class Jwt {
    @Getter
    private final String token;

    private Jwt(String token) {
        this.token = token;
    }

    public static Jwt of(Long userId, Long validityInMinutes, String secretKey) {
        var issueDate = Instant.now();
        return new Jwt(Jwts.builder()
                .claim("user_id", userId)
                .setIssuedAt(Date.from(issueDate))
                .setExpiration(Date.from(issueDate.plus(validityInMinutes, ChronoUnit.MINUTES)))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .compact());
    }

    public static Jwt of(String token) {
        return new Jwt(token);
    }


    public static Long from(final String token, final String secretKey) {


        Jws<Claims> jws;
        try {
            jws = Jwts.parserBuilder().
                    setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                    .build().parseClaimsJws(token);
        } catch (JwtException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage() + " -T");
        }

        return jws.getBody().get("user_id", Long.class);
    }

}

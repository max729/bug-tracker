package com.bug_tracker.auth;

import lombok.Getter;


public class Login {
    @Getter
    private final Jwt accessJwt;
    @Getter
    private final Jwt refreshJwt;

    private static final Long ACCESS_TOKEN_VALIDITY = 1L;
    private static final Long REFRESH_TOKEN_VALIDITY = 2880L;

    private Login(Jwt accessJwt, Jwt refreshJwt) {
        this.accessJwt = accessJwt;
        this.refreshJwt = refreshJwt;
    }

    public static Login of(final String userId,final String accessSecret,final String refreshSecret){
        return new Login(
                Jwt.of(userId, ACCESS_TOKEN_VALIDITY, accessSecret),
                Jwt.of(userId, REFRESH_TOKEN_VALIDITY, refreshSecret)
        );
    }

    public static Login of(final String userId,final String accessSecret,final Jwt refreshSecret){
        return new Login(
                Jwt.of(userId, ACCESS_TOKEN_VALIDITY, accessSecret),
                refreshSecret
        );
    }

}

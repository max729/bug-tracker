package com.bug_tracker.app_user;

import lombok.Getter;


public class Login {
    @Getter
    private final Token accessToken;
    @Getter
    private final Token refreshToken;

    private static final Long ACCESS_TOKEN_VALIDITY = 1L;
    private static final Long REFRESH_TOKEN_VALIDITY = 2880L;

    private Login(Token accessToken, Token refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static Login of(final Long userId,final String accessSecret,final String refreshSecret){
        return new Login(
                Token.of(userId, ACCESS_TOKEN_VALIDITY, accessSecret),
                Token.of(userId, REFRESH_TOKEN_VALIDITY, refreshSecret)
        );
    }

    public static Login of(final Long userId,final String accessSecret,final Token refreshSecret){
        return new Login(
                Token.of(userId, ACCESS_TOKEN_VALIDITY, accessSecret),
                refreshSecret
        );
    }

}

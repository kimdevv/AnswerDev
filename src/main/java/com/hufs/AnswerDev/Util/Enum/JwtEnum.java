package com.hufs.AnswerDev.Util.Enum;

public enum JwtEnum {
    ACCESSTOKEN("accessToken"),
    REFRESHTOKEN("refreshToken");
    private final String tokenType;

    JwtEnum(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getTokenType() {
        return tokenType;
    }
}

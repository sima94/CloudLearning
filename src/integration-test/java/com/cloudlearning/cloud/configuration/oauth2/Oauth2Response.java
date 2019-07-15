package com.cloudlearning.cloud.configuration.oauth2;

import lombok.Data;

@Data
public class Oauth2Response {

    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private Long expiresIn;
    private String scope;

    public String accessToken() {
        return this.tokenType + " " + this.accessToken;
    }
}

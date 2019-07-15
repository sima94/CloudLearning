package com.cloudlearning.cloud.configuration.oauth2;

import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Data
public class Oauth2Request {

    private String grantType;
    private String username;
    private String password;
    private String refreshToken;

    public Oauth2Request(String grantType, String username, String password) {
        this.grantType = grantType;
        this.username = username;
        this.password = password;
    }

    public Oauth2Request(String grantType, String refreshToken) {
        this.grantType = grantType;
        this.refreshToken = refreshToken;
    }

    public MultiValueMap<String, String> toJson(){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", grantType);
        params.add("client_id", "spring-security-oauth2-read-write-client");
        params.add("username", username);
        params.add("password", password);
        params.add("refresh_token", refreshToken);
        return  params;
    }
}

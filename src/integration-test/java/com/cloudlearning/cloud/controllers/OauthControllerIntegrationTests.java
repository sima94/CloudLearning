package com.cloudlearning.cloud.controllers;

import com.cloudlearning.cloud.configuration.oauth2.Oauth2Request;
import com.cloudlearning.cloud.configuration.oauth2.Oauth2Response;
import com.cloudlearning.cloud.controllers.base.AbstractControllerIntegrationTests;
import org.junit.Test;

public class OauthControllerIntegrationTests extends AbstractControllerIntegrationTests {

    @Test
    public void whenTryToLogin_WithCorrectCredential_ThenReturnNewOauthTokens() throws Exception {

        Oauth2Request request = new Oauth2Request("password", "admin", "admin1234");
        Oauth2Response response = oauth2service.getToken(request);

        assert response.getAccessToken().length() > 0;
    }

    @Test
    public void whenTryToRefers_WithValidRefreshToken_ThenReturnNewRefreshedOauthTokens() throws Exception {

        Oauth2Request loginRequest = new Oauth2Request("password", "admin", "admin1234");
        Oauth2Response loginResponse = oauth2service.getToken(loginRequest);

        Oauth2Request refreshRequest = new Oauth2Request("refresh_token", loginResponse.getRefreshToken());
        Oauth2Response refreshResponse = oauth2service.getToken(refreshRequest);

        assert refreshResponse.getAccessToken().length() > 0;
        assert refreshResponse.getRefreshToken().length() > 0;
    }

}

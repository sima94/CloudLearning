package com.cloudlearning.cloud.controllers.base;

import com.cloudlearning.cloud.configuration.base.AbstractIntegrationTests;
import com.cloudlearning.cloud.configuration.oauth2.Oauth2Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
public abstract class AbstractControllerIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    protected Oauth2Services oauth2service;

    @Autowired
    protected MockMvc mvc;
}

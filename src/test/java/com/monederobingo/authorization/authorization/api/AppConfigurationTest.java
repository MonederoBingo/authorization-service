package com.monederobingo.authorization.authorization.api;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.*;

public class AppConfigurationTest {

    private AppConfiguration appConfiguration;

    @Before
    public void setup() {
        appConfiguration = new AppConfiguration();
    }

    @Test
    public void shouldReturnNonNull() {
        // when
        UserDetailsService userDetailsService = appConfiguration.getUserDetailsService();

        // then
        assertNotNull(userDetailsService);
    }

}
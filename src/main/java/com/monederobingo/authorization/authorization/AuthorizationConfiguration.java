package com.monederobingo.authorization.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;

@Configuration
public class AuthorizationConfiguration extends GlobalAuthenticationConfigurerAdapter
{
    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.authenticationProvider(authenticationProvider);
    }
}

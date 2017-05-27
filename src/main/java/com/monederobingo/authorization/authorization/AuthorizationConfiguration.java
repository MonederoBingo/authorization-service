package com.monederobingo.authorization.authorization;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;

@Configuration
public class AuthorizationConfiguration extends GlobalAuthenticationConfigurerAdapter
{

    @Override public void init(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.inMemoryAuthentication()
                .withUser("json_user")
                .roles("company")
                .password("password");
    }
}

package com.monederobingo.authorization.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import xyz.greatapp.libs.service.context.ThreadContextService;
import xyz.greatapp.libs.service.context.ThreadContextServiceImpl;
import xyz.greatapp.libs.service.filters.ContextFilter;

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

    @Bean
    public ThreadContextService getThreadContextService() {
        return new ThreadContextServiceImpl();
    }

    @Bean
    public ContextFilter getContextFilter() {
        return new ContextFilter(getThreadContextService());
    }
}

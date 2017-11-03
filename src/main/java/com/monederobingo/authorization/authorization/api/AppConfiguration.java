package com.monederobingo.authorization.authorization.api;

import com.monederobingo.authorization.authorization.api.filters.SecurityFilter;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import xyz.greatapp.libs.service.context.ThreadContextService;
import xyz.greatapp.libs.service.context.ThreadContextServiceImpl;
import xyz.greatapp.libs.service.filters.ContextFilter;
import xyz.greatapp.libs.service.location.ServiceLocator;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

@Configuration
public class AppConfiguration extends GlobalAuthenticationConfigurerAdapter
{
    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(getUserDetailsService()).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    UserDetailsService getUserDetailsService() {
        return username ->
        {
            JSONObject user = new JSONObject(getAuthorizationService().getUser(username).getObject());
            if(user.has("email")) {
                return new CustomUserDetails(
                        user.getString("email"),
                        user.getString("password"),
                        true, true, true, true,
                        createAuthorityList("ROLE_USER"),
                        Integer.toString(user.getInt("company_user_id")),
                        Integer.toString(user.getInt("company_id")));
            } else {
                throw new BadCredentialsException("BadCredentialsException");
            }
        };
    }

    @Bean
    public ThreadContextService getThreadContextService()
    {
        return new ThreadContextServiceImpl();
    }

    @Bean
    public ServiceLocator getServiceLocator()
    {
        return new ServiceLocator();
    }

    @Bean
    public ContextFilter getContextFilter(ThreadContextService threadContextService)
    {
        return new ContextFilter(threadContextService);
    }

    @Bean
    public SecurityFilter getSecurityFilter()
    {
        return new SecurityFilter();
    }

    @Bean
    public AuthorizationService getAuthorizationService()
    {
        return new AuthorizationService();
    }
}

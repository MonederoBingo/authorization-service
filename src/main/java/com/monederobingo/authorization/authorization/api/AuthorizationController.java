package com.monederobingo.authorization.authorization.api;

import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@EnableResourceServer
public class AuthorizationController
{
    @RequestMapping(method = GET, value = "/user")
    public Principal getUser(Principal user)
    {
        return user;
    }
}

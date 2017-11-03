package com.monederobingo.authorization.authorization.api;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User
{
    private final String userId;
    private final String companyId;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,
                             String userId, String companyId)
    {
        super(username, password, authorities);
        this.userId = userId;
        this.companyId = companyId;
    }

    public CustomUserDetails(String username, String password, boolean enabled, boolean accountNonExpired,
                             boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
                             String userId, String companyId)
    {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId = userId;
        this.companyId = companyId;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getCompanyId() {
        return companyId;
    }
}

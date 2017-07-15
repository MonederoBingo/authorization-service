package com.monederobingo.authorization.authorization;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import netscape.javascript.JSObject;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider
{

    @Override public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        CompanyUser user = new CompanyUser();
        try
        {
            user = getUser(name, password);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        if (user.getEmail() == null)
        {
            throw new BadCredentialsException("BadCredentialsException");
        }
        return new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    private CompanyUser getUser(String email, String password) throws SQLException
    {
        final String DATABASE_SERVICE_URL = "http://test.localhost:30001/";

        String sql = "SELECT company_user.* FROM company_user" +
                " WHERE company_user.email = '"+ email + "'" +
                " AND company_user.password = '" + password + "';";

        HttpEntity<SelectQuery> entity = new HttpEntity<>(
                new SelectQuery(sql),
                getHttpHeaders());
        ResponseEntity<DatabaseServiceResult> responseEntity = getRestTemplate().postForEntity(
                DATABASE_SERVICE_URL + "/select",
                entity,
                DatabaseServiceResult.class);

        Object object = responseEntity.getBody().getObject();
        if(object == null)
        {
            return new CompanyUser();
        }
        return buildCompanyUser(new JSONObject(object.toString()));
    }

    private HttpHeaders getHttpHeaders()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private RestTemplate getRestTemplate()
    {
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> list = new ArrayList<>();
        list.add(new MappingJackson2HttpMessageConverter());
        restTemplate.setMessageConverters(list);
        return restTemplate;
    }

    @Override public boolean supports(Class<?> authentication)
    {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
    }

    private CompanyUser buildCompanyUser(JSONObject object) throws SQLException
    {
        CompanyUser companyUser = new CompanyUser();
        companyUser.setCompanyUserId(object.getLong("company_user_id"));
        companyUser.setCompanyId(object.getLong("company_id"));
        companyUser.setName(object.getString("name"));
        companyUser.setEmail(object.getString("email"));
        companyUser.setPassword(object.getString("password"));
        companyUser.setActive(object.getBoolean("active"));
        companyUser.setActivationKey(object.getString("activation_key"));
        companyUser.setLanguage(object.getString("language"));
        companyUser.setMustChangePassword(object.getBoolean("must_change_password"));
        companyUser.setApiKey(object.getString("api_key"));
        return companyUser;
    }
}

package com.monederobingo.authorization.authorization;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
import xyz.greatapp.libs.service.context.ThreadContextService;
import xyz.greatapp.libs.service.requests.database.ColumnValue;
import xyz.greatapp.libs.service.requests.database.SelectQueryRQ;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final EurekaClient eurekaClient;
    private final ThreadContextService threadContextService;

    @Autowired
    public CustomAuthenticationProvider(EurekaClient eurekaClient,
                                        ThreadContextService threadContextService) {
        this.eurekaClient = eurekaClient;
        this.threadContextService = threadContextService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        CompanyUser user = new CompanyUser();
        try {
            user = getUser(name, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (user.getEmail() == null) {
            throw new BadCredentialsException("BadCredentialsException");
        }
        return new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    private CompanyUser getUser(String email, String password) throws SQLException {
        ColumnValue[] filters = new ColumnValue[]{
                new ColumnValue("email", email),
                new ColumnValue("password", password)
        };

        HttpEntity<SelectQueryRQ> entity = new HttpEntity<>(
                new SelectQueryRQ("company_user", filters),
                getHttpHeaders());
        ResponseEntity<DatabaseServiceResult> responseEntity = getRestTemplate().postForEntity(
                getDatabaseURL() + "/select",
                entity,
                DatabaseServiceResult.class);

        Object object = responseEntity.getBody().getObject();
        if (object == null) {
            return new CompanyUser();
        }
        return buildCompanyUser(new JSONObject(object.toString()));
    }

    private String getDatabaseURL() {
        InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("database", false);
        String homePageUrl = instanceInfo.getHomePageUrl();
        boolean hasHttps = homePageUrl.contains("https://");
        homePageUrl = homePageUrl.replace("http://", "");
        homePageUrl = homePageUrl.replace("https://", "");
        homePageUrl = threadContextService.getThreadContext().getEnvironment().getURIPrefix() + homePageUrl;
        return hasHttps ? "https://" + homePageUrl : "http://" + homePageUrl;
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> list = new ArrayList<>();
        list.add(new MappingJackson2HttpMessageConverter());
        restTemplate.setMessageConverters(list);
        return restTemplate;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
    }

    private CompanyUser buildCompanyUser(JSONObject object) throws SQLException {
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

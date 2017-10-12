package com.monederobingo.authorization.authorization.api.filters;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static java.lang.Integer.MIN_VALUE;
import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;
import static javax.servlet.http.HttpServletResponse.SC_OK;

@Component
@Order(MIN_VALUE + 1)
public class SecurityFilter extends GenericFilterBean
{
    private final List<String> WHITE_LIST_CLIENTS = asList(
            "http://localhost:8080",
            "http://localhost",
            "https://localhost:8080",
            "https://localhost",
            "http://test.localhost",
            "https://test.localhost",
            "http://test.localhost:8080",
            "https://test.localhost:8080",
            "http://www.monederobingo.com",
            "https://www.monederobingo.com",
            "http://uat.monederobingo.com",
            "https://uat.monederobingo.com");

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (isValidClientUrl(getClientUrl(httpServletRequest)))
        {
            setCORSHeaders(httpServletResponse, httpServletRequest);
        }
        if ("OPTIONS".equalsIgnoreCase(httpServletRequest.getMethod())) {
            httpServletResponse.setStatus(SC_OK);
        } else {
            chain.doFilter(request, response);
        }
    }

    private void setCORSHeaders(HttpServletResponse response, HttpServletRequest request)
    {
        if(response.getHeader("Access-Control-Allow-Headers") == null) response.addHeader("Access-Control-Allow-Headers", "Content-Type,x-requested-with,Authorization");
        if(response.getHeader("Access-Control-Max-Age") == null) response.addHeader("Access-Control-Max-Age", "360");
        if(response.getHeader("Access-Control-Allow-Methods") == null) response.addHeader("Access-Control-Allow-Methods", "GET,POST,DELETE,PUT");
        if(response.getHeader("Access-Control-Allow-Origin") == null) response.addHeader("Access-Control-Allow-Origin", getClientUrl(request));
        if(response.getHeader("Access-Control-Allow-Credentials") == null) response.addHeader("Access-Control-Allow-Credentials", "true");
    }

    private boolean isValidClientUrl(String serverName)
    {
        return WHITE_LIST_CLIENTS.contains(serverName);
    }

    private String getClientUrl(HttpServletRequest request)
    {
        if (nonNull(request.getHeader("origin")))
        {
            return request.getHeader("origin");
        }
        else if (nonNull(request.getHeader("referer")))
        {
            return request.getHeader("referer");
        }
        return "";
    }
}

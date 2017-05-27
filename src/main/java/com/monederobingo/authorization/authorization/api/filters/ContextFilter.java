package com.monederobingo.authorization.authorization.api.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.monederobingo.authorization.authorization.common.Environment;
import com.monederobingo.authorization.authorization.common.context.ThreadContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContextFilter implements Filter
{

    private final ThreadContextService threadContextService;

    @Autowired
    public ContextFilter(ThreadContextService threadContextService)
    {
        this.threadContextService = threadContextService;
    }

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        initializeContext(httpServletRequest, httpServletResponse);
        chain.doFilter(request, response);
    }

    private void initializeContext(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        threadContextService.initializeContext(getEnvironment(httpServletRequest), httpServletRequest.getHeader("language"));
        setHeaders(httpServletResponse, httpServletRequest);
    }

    private void setHeaders(HttpServletResponse response, HttpServletRequest request) {
        List<String> incomingURLs = Arrays.asList(
                "http://localhost:8080", //
                "http://test.localhost:8080", //
                "http://www.monederobingo.com", //
                "http://test.monederobingo.com");
        String clientOrigin = request.getHeader("origin") != null ? request.getHeader("origin") : request.getHeader("referer");
        int myIndex = incomingURLs.indexOf(clientOrigin);
        if (myIndex != -1) {
            response.addHeader("Access-Control-Allow-Headers", "Content-Type,x-requested-with,Authorization,Api-Key,User-Id,language");
            response.addHeader("Access-Control-Max-Age", "360");
            response.addHeader("Access-Control-Allow-Methods", "GET,POST,DELETE,PUT");
            response.addHeader("Access-Control-Allow-Origin", clientOrigin);
            response.addHeader("Access-Control-Allow-Credentials", "true");
            response.addHeader("Access-Control-Allow-Credentials", "true");
        }
    }

    private Environment getEnvironment(HttpServletRequest request) {
        switch (request.getServerName()) {
            case "services.monederobingo.com":
                return Environment.PRODUCTION;
            case "test.services.monederobingo.com":
                return Environment.UAT;
            case "test.localhost":
                return Environment.TEST;
            default:
                return Environment.DEV;
        }
    }
}

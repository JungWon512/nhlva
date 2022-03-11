package com.ishift.auction.interceptor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomBeforeAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public CustomBeforeAuthenticationFilter() {
        setUsernameParameter("email");
        super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        String email = request.getParameter("email");

        //System.out.println("The user " + email + " is about to login");

        // run custom logics...

        return super.attemptAuthentication(request, response);
    }
}
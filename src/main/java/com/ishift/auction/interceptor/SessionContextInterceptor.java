package com.ishift.auction.interceptor;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
public class SessionContextInterceptor implements HandlerInterceptor {

//    private final JwtAuthTokenProvider jwtAuthTokenProvider;
//    private static final String AUTHORIZATION_HEADER ="x-auth-token";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

//
//        RequestCache requestCache = new HttpSessionRequestCache();
//        SavedRequest savedRequest = requestCache.getRequest(request,response);
//
//        if( savedRequest!=null && savedRequest.getRedirectUrl() != null&&!savedRequest.getRedirectUrl().contains("/assets/")) {
//            request.getSession().setAttribute("returnUrl",savedRequest.getRedirectUrl());
//            log.info("PATH====>>"+savedRequest.getRedirectUrl());
////            response.sendRedirect("/user/login");
//            return true;
//        }


        request.setAttribute("start",System.currentTimeMillis());
        if(request.getSession()!=null && request.getSession().getAttribute("country")!=null) {
            request.setAttribute("country", request.getSession().getAttribute("country"));
        }

        if(!"/user/login".equals(request.getRequestURI()) && !"/error".equals(request.getRequestURI())) {
            request.setAttribute("returnUrl", request.getRequestURI());
        }
        return true;


    }

}

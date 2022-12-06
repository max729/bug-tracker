package com.bug_tracker.interceptor;

import com.bug_tracker.app_user.AppUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final AppUserService appUserService;

    public AuthorizationInterceptor(AppUserService appUserService){
        this.appUserService = appUserService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){

        String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer "))
            throw new ResponseStatusException(HttpStatus.PROXY_AUTHENTICATION_REQUIRED,"not login");

        request.setAttribute("appUser", appUserService.getUserFromToken(authorizationHeader.substring(7)));

        return true;
    }

}

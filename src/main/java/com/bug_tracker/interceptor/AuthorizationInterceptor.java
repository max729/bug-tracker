package com.bug_tracker.interceptor;

import com.bug_tracker.app_user.AppUser;
import com.bug_tracker.app_user.AppUserService;
import com.bug_tracker.app_user.UserRole;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
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
        //Logger logger = LoggerFactory.getLogger(AuthorizationInterceptor.class);

        String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer "))
            throw new ResponseStatusException(HttpStatus.PROXY_AUTHENTICATION_REQUIRED,"not login");

        var appUser = appUserService.getUserFromToken(authorizationHeader.substring(7));

        request.setAttribute("appUser", appUser);


        if(  !request.getMethod().equals("GET")  && appUser.getUserRole() == UserRole.GUEST    ){
            throw new  ResponseStatusException(HttpStatus.FORBIDDEN,"GUEST can not modify");
        }


        return true;
    }

}

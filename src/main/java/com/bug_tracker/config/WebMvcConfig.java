package com.bug_tracker.config;

import com.bug_tracker.interceptor.AuthorizationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final AuthorizationInterceptor authorizationInterceptor;

    public WebMvcConfig(AuthorizationInterceptor authorizationInterceptor) {
        this.authorizationInterceptor = authorizationInterceptor;
    }

/*     @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**").allowedOrigins("http://localhost:8081/");
    } */

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry
                .addInterceptor(authorizationInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/appUsers/login"
                        ,"/api/appUsers/register"
                        ,"/api/appUsers/refresh"
                        ,"/api/appUsers/forgot"
                        ,"/api/appUsers/reset"
                        ,"/api/appUsers/logout");
    }
}

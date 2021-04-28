package com.cr.campusmicroblog.configuration;

import com.cr.campusmicroblog.interceptor.LoginRequiredInterceptor;
import com.cr.campusmicroblog.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author cr
 * @date 2020-11-17 13:06
 */
@Configuration
public class CampusMicroBlogWebConfiguration implements WebMvcConfigurer {
    @Autowired
    private PassportInterceptor passportInterceptor;

    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/setting*");
    }
}

package com.nowcoder.configuration;

import com.nowcoder.interceptor.LoginRequredIntercepter;
import com.nowcoder.interceptor.PassportIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class WendaWebConfigration extends WebMvcConfigurerAdapter {
    @Autowired
    PassportIntercepter passportIntercepter;

    @Autowired
    LoginRequredIntercepter loginRequredIntercepter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportIntercepter);
        registry.addInterceptor(loginRequredIntercepter).addPathPatterns("/user/*");

        super.addInterceptors(registry);
    }
}

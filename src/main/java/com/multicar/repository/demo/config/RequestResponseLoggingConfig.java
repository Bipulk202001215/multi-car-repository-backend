package com.multicar.repository.demo.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class RequestResponseLoggingConfig {

    @Bean
    public RequestResponseLoggingFilter requestResponseLoggingFilter() {
        return new RequestResponseLoggingFilter();
    }

    @Bean
    public FilterRegistrationBean<RequestResponseLoggingFilter> requestResponseLoggingFilterRegistration(
            RequestResponseLoggingFilter filter) {
        FilterRegistrationBean<RequestResponseLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }
}


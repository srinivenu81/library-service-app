package com.tech.springbootlibraryservice.config;

import com.okta.spring.boot.oauth.Okta;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filerChain(HttpSecurity httpSecurity) throws Exception {

        //Disable cross site request forgery
        httpSecurity.csrf().disable();

        httpSecurity.authorizeRequests(configurer ->
                        configurer.antMatchers("/api/books/secure/**",
                                        "/api/reviews/secure/**")
                                .authenticated())
                .oauth2ResourceServer().jwt();

        //Add Cors Filters
        httpSecurity.cors();

        httpSecurity.setSharedObject(ContentNegotiationStrategy.class
                , new HeaderContentNegotiationStrategy());

        //Force a non-empty response body for 401's to make response friendly
        Okta.configureResourceServer401ResponseBody(httpSecurity);
        return httpSecurity.build();
    }
}

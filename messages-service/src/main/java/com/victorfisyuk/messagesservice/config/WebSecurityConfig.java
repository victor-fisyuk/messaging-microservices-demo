package com.victorfisyuk.messagesservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String SERVICE_CLIENTS_PRINCIPAL_NAME = "service-client";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf()
                    .disable()
                .authorizeRequests()
                    .mvcMatchers(HttpMethod.GET, "/actuator/health").permitAll()
                    .mvcMatchers(HttpMethod.GET, "/message/**").hasAuthority("SCOPE_messages:read")
                    .mvcMatchers(HttpMethod.POST, "/message/**").hasAuthority("SCOPE_messages:write")
                    .anyRequest().denyAll()
                    .and()
                .oauth2ResourceServer()
                    .jwt()
                        .jwtAuthenticationConverter(
                                new PrincipalNameChangingUserJwtAuthenticationConverter(SERVICE_CLIENTS_PRINCIPAL_NAME));
        // @formatter:on
    }
}

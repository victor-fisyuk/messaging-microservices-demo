package com.victorfisyuk.profilesservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf()
                    .disable()
                .authorizeRequests()
                    .mvcMatchers(HttpMethod.GET, "/actuator/health").permitAll()
                    .mvcMatchers(HttpMethod.GET, "/profile/**").hasAuthority("SCOPE_profiles:read")
                    .mvcMatchers(HttpMethod.POST, "/profile/**").hasAuthority("SCOPE_profiles:write")
                    .mvcMatchers(HttpMethod.PUT, "/profile/**").hasAuthority("SCOPE_profiles:write")
                    .anyRequest().denyAll()
                    .and()
                .oauth2ResourceServer()
                    .jwt();
        // @formatter:on
    }
}

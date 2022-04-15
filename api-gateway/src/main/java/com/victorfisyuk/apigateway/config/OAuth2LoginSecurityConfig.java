package com.victorfisyuk.apigateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesRegistrationAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebFluxSecurity
public class OAuth2LoginSecurityConfig {
    private static final String END_SESSION_URI_PROPERTY_FORMAT = "spring.security.oauth2.client.provider.%s.end-session-uri";

    private final OAuth2ClientProperties clientProperties;
    private final Environment environment;

    @Autowired
    public OAuth2LoginSecurityConfig(
            OAuth2ClientProperties clientProperties,
            Environment environment
    ) {
        this.clientProperties = clientProperties;
        this.environment = environment;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        // @formatter:off
        http
                .csrf().disable()
                .authorizeExchange()
                    .pathMatchers("/actuator/health").permitAll()
                    .pathMatchers("/api/profiles/**").permitAll()
                    .anyExchange().authenticated()
                    .and()
                .oauth2Client(withDefaults())
                .oauth2Login(withDefaults())
                .logout()
                    .logoutSuccessHandler(oidcLogoutSuccessHandler());
        // @formatter:on
        return http.build();
    }

    @Bean
    public ServerOAuth2AuthorizedClientRepository authorizedClientRepository() {
        return new WebSessionServerOAuth2AuthorizedClientRepository();
    }

    /**
     * Workaround for the issue with {@link OidcClientInitiatedServerLogoutSuccessHandler} which doesn't work in case
     * when the OAuth 2.0 provider configuration doesn't have <i>issuer-uri</i> property set.
     *
     * @see <a href="https://github.com/spring-projects/spring-security/issues/10059">Spring Security Issue 10059</a>
     *
     * @return client registration repository
     */
    @Bean
    public InMemoryReactiveClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = new ArrayList<>(
                OAuth2ClientPropertiesRegistrationAdapter.getClientRegistrations(clientProperties).values());

        var registrationsWithMetadata = registrations.stream()
                .map(clientRegistration -> {
                    String endSessionEndpoint = Optional.ofNullable(clientProperties.getRegistration()
                                    .getOrDefault(clientRegistration.getRegistrationId(), null))
                            .map(OAuth2ClientProperties.Registration::getProvider)
                            .map(provider -> String.format(END_SESSION_URI_PROPERTY_FORMAT, provider))
                            .map(environment::getProperty)
                            .orElse(null);
                    return ClientRegistration.withClientRegistration(clientRegistration)
                            .providerConfigurationMetadata(Collections.singletonMap("end_session_endpoint",
                                    endSessionEndpoint))
                            .scope(clientRegistration.getScopes())
                            .build();
                })
                .collect(Collectors.toList());
        return new InMemoryReactiveClientRegistrationRepository(registrationsWithMetadata);
    }

    private ServerLogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedServerLogoutSuccessHandler oidcLogoutSuccessHandler =
                new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository());

        // Sets the location that the End-User's User Agent will be redirected to
        // after the logout has been performed at the Provider
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}/login");

        return oidcLogoutSuccessHandler;
    }
}

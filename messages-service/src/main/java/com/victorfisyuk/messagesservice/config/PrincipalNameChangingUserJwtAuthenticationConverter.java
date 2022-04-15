package com.victorfisyuk.messagesservice.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.method.annotation.OAuth2AuthorizedClientArgumentResolver;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * Workaround for the issue of using {@link RegisteredOAuth2AuthorizedClient} and <i>Token Relay</i> pattern when
 * the service app is a <i>Resource Server</i> and an <i>OAuth 2.0 client</i> with <i>Client Credentials</i> grant type
 * at the same time.
 *
 * <p/>When the service app is being called by API Gateway, it receives OAuth 2.0 end-user token and the principal name
 * in the {@link Authentication} object is set to the end-user name. When {@link OAuth2AuthorizedClientArgumentResolver}
 * is resolving {@link RegisteredOAuth2AuthorizedClient} for an <i>OAuth 2.0 client</i> with
 * <i>Client Credentials</i> grant type it uses that end-user name as the principal name for the authorization
 * request. Because of that {@link OAuth2AuthorizedClientArgumentResolver} requests a separate access token and creates
 * a separate {@link OAuth2AuthorizedClient} object for each end-user. However, an <i>OAuth 2.0 client</i> with
 * <i>Client Credentials</i> grant type, which is used for service-to-service communication, needs only a single access
 * token, which does not depend on end-users.
 */
public class PrincipalNameChangingUserJwtAuthenticationConverter
        implements Converter<Jwt, AbstractAuthenticationToken> {
    private final String principalName;
    private final JwtAuthenticationConverter delegate = new JwtAuthenticationConverter();

    public PrincipalNameChangingUserJwtAuthenticationConverter(String principalName) {
        this.principalName = principalName;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        AbstractAuthenticationToken authenticationToken = delegate.convert(source);
        return new JwtAuthenticationToken(source, authenticationToken.getAuthorities(), principalName);
    }
}

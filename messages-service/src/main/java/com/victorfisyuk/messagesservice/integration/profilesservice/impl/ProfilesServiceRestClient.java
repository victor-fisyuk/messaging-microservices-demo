package com.victorfisyuk.messagesservice.integration.profilesservice.impl;

import com.victorfisyuk.messagesservice.integration.profilesservice.ProfileDTO;
import com.victorfisyuk.messagesservice.integration.profilesservice.ProfilesServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Optional;

import static com.victorfisyuk.messagesservice.message.MessageController.MESSAGES_SERVICE_CLIENT_REGISTRATION_ID;
import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@Slf4j
@Service
public class ProfilesServiceRestClient implements ProfilesServiceClient {
    private static final String PROFILE_URL = "http://profiles-service/api/profiles/profile?userId={userId}";

    private final WebClient webClient;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    public ProfilesServiceRestClient(WebClient webClient, OAuth2AuthorizedClientService authorizedClientService) {
        this.webClient = webClient;
        this.authorizedClientService = authorizedClientService;
    }

    @Override
    public Optional<ProfileDTO> getProfile(String userId) {
        log.debug("Calling {}", PROFILE_URL);

        var uriVariables = Map.ofEntries(
                Map.entry("userId", userId)
        );
        return Optional.ofNullable(webClient.get()
                .uri(PROFILE_URL, uriVariables)
                .attributes(oauth2AuthorizedClient(getMessagesServiceOAuth2AuthorizedClient()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ProfileDTO>(){})
                .block());
    }

    private OAuth2AuthorizedClient getMessagesServiceOAuth2AuthorizedClient() {
        Authentication authentication = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .orElseThrow();
        String principal = authentication.getName();
        return authorizedClientService.loadAuthorizedClient(MESSAGES_SERVICE_CLIENT_REGISTRATION_ID, principal);
    }
}

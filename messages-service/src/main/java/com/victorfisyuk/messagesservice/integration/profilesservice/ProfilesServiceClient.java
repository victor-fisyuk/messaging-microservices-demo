package com.victorfisyuk.messagesservice.integration.profilesservice;

import java.util.Optional;

/**
 * Provides methods to call <i>profiles service</i> endpoints.
 */
public interface ProfilesServiceClient {
    /**
     * Get profile of the specified user.
     *
     * @param userId user id
     * @return user profile
     */
    Optional<ProfileDTO> getProfile(String userId);
}

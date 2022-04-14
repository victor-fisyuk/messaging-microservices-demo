package com.victorfisyuk.profilesservice.profile;

import java.util.function.UnaryOperator;

/**
 * Provides methods to work with profiles.
 */
public interface ProfileService {
    /**
     * Get profile of the specified user.
     *
     * @param userId user id
     * @return user profile
     */
    Profile getProfile(String userId);

    /**
     * Create a new profile.
     *
     * @param profile profile model
     * @return created profile
     */
    Profile createProfile(Profile profile);

    /**
     * Update existing profile.
     *
     * @param profileId profile Id
     * @param updater operator to update the profile
     * @return updated profile
     */
    Profile updateProfile(long profileId, UnaryOperator<Profile> updater);
}

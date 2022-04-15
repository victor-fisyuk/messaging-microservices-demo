package com.victorfisyuk.profilesservice.profile.impl;

import com.victorfisyuk.profilesservice.profile.Profile;
import com.victorfisyuk.profilesservice.profile.ProfileRepository;
import com.victorfisyuk.profilesservice.profile.ProfileService;
import com.victorfisyuk.profilesservice.profile.exception.ProfileNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.UnaryOperator;

@Slf4j
@Transactional
@Service
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Cacheable("profile-by-userid")
    @Override
    public Profile getProfile(String userId) {
        return profileRepository.findByUserId(userId);
    }

    @CachePut(cacheNames = "profile-by-userid", key = "#result.userId")
    @Override
    public Profile createProfile(Profile profile) {
        return profileRepository.save(profile);
    }

    @CachePut(cacheNames = "profile-by-userid", key = "#result.userId")
    @Override
    public Profile updateProfile(long profileId, UnaryOperator<Profile> updater) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(profileId));

        log.info("Updating profile {}", profileId);

        return updater.apply(profile);
    }
}

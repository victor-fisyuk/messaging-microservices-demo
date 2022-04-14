package com.victorfisyuk.profilesservice.profile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.victorfisyuk.profilesservice.profile.dto.ProfileCreationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Imports initial profiles into the database.
 * <br/>The profiles list are passed using a property initialized by Terraform.
 */
@Slf4j
@Service
public class ProfileImportService {
    private final String initialProfiles;
    private final ObjectMapper objectMapper;
    private final ProfileMapper profileMapper;
    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileImportService(
            @Value("${initial-profiles}") String initialProfiles,
            ObjectMapper objectMapper,
            ProfileMapper profileMapper,
            ProfileRepository profileRepository
    ) {
        this.initialProfiles = initialProfiles;
        this.objectMapper = objectMapper;
        this.profileMapper = profileMapper;
        this.profileRepository = profileRepository;
    }

    @PostConstruct
    public void importInitialProfiles() {
        if (initialProfiles.isBlank()) {
            return;
        }

        try {
            List<ProfileCreationDTO> profiles = objectMapper.readValue(initialProfiles, new TypeReference<>() {});
            profiles.forEach(profile -> importProfile(profileMapper.convertToModel(profile)));
        } catch (JsonProcessingException e) {
            log.error("Failed to import initial profiles", e);
        }
    }

    private void importProfile(Profile profile) {
        if (profileRepository.findByUserId(profile.getUserId()) == null) {
            log.info("Importing profile - {}", profile);
            profileRepository.save(profile);
        } else {
            log.info("Profile already exists, skipping import - {}", profile);
        }
    }
}

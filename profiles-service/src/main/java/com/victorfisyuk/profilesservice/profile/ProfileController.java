package com.victorfisyuk.profilesservice.profile;

import com.victorfisyuk.profilesservice.profile.dto.ProfileCreationDTO;
import com.victorfisyuk.profilesservice.profile.dto.ProfileDTO;
import com.victorfisyuk.profilesservice.profile.dto.ProfileUpdatingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;
    private final ProfileMapper profileMapper;

    @Autowired
    public ProfileController(ProfileService profileService, ProfileMapper profileMapper) {
        this.profileService = profileService;
        this.profileMapper = profileMapper;
    }

    @GetMapping
    public ProfileDTO getProfile(@RequestParam("userId") String userId) {
        return profileMapper.convertToDTO(profileService.getProfile(userId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileDTO createProfile(@Valid @RequestBody ProfileCreationDTO profileCreationDTO) {
        Profile profile = profileMapper.convertToModel(profileCreationDTO);
        return profileMapper.convertToDTO(profileService.createProfile(profile));
    }

    @PutMapping("/{profileId}")
    public ProfileDTO updateProfile(
            @PathVariable("profileId") long profileId,
            @Valid @RequestBody ProfileUpdatingDTO profileUpdatingDTO
    ) {
        return profileMapper.convertToDTO(profileService.updateProfile(profileId, profile ->
                profileMapper.updateModelFromDTO(profile, profileUpdatingDTO)));
    }
}

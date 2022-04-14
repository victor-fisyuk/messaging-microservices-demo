package com.victorfisyuk.profilesservice.profile;

import com.victorfisyuk.profilesservice.config.ModelMapperConfig;
import com.victorfisyuk.profilesservice.profile.dto.ProfileCreationDTO;
import com.victorfisyuk.profilesservice.profile.dto.ProfileDTO;
import com.victorfisyuk.profilesservice.profile.dto.ProfileUpdatingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = ModelMapperConfig.class)
public interface ProfileMapper {
    ProfileDTO convertToDTO(Profile profile);

    @Mapping(target = "id", ignore = true)
    Profile convertToModel(ProfileCreationDTO profileCreationDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    Profile updateModelFromDTO(@MappingTarget Profile profile, ProfileUpdatingDTO profileUpdatingDTO);
}

package com.victorfisyuk.profilesservice.config;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        typeConversionPolicy = ReportingPolicy.ERROR,
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        disableSubMappingMethodsGeneration = true
)
public class ModelMapperConfig {
}

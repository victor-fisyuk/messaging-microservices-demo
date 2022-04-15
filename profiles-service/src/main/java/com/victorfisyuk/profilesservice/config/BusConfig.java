package com.victorfisyuk.profilesservice.config;

import com.victorfisyuk.profilesservice.profile.bus.ProfileCreatedEvent;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@RemoteApplicationEventScan(basePackageClasses = ProfileCreatedEvent.class)
public class BusConfig {
}

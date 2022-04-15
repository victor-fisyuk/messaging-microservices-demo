package com.victorfisyuk.messagesservice.config;

import com.victorfisyuk.messagesservice.integration.profilesservice.bus.ProfileCreatedEvent;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@RemoteApplicationEventScan(basePackageClasses = ProfileCreatedEvent.class)
public class BusConfig {
}

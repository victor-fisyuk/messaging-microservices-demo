package com.victorfisyuk.profilesservice.profile.bus;

import lombok.Getter;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;

@Getter
public class ProfileCreatedEvent extends RemoteApplicationEvent {
    private static final Object TRANSIENT_SOURCE = new Object();

    private final long profileId;
    private final String userId;

    public ProfileCreatedEvent(String originService, String destination, long profileId, String userId) {
        super(TRANSIENT_SOURCE, originService, DEFAULT_DESTINATION_FACTORY.getDestination(destination));
        this.profileId = profileId;
        this.userId = userId;
    }
}

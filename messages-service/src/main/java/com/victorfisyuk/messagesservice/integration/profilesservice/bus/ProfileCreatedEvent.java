package com.victorfisyuk.messagesservice.integration.profilesservice.bus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;

@ToString
@Getter
@Setter
public class ProfileCreatedEvent extends RemoteApplicationEvent {
    private long profileId;
    private String userId;
}

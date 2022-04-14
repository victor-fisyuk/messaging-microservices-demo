package com.victorfisyuk.messagesservice.message;

import com.victorfisyuk.messagesservice.config.ModelMapperConfig;
import com.victorfisyuk.messagesservice.integration.profilesservice.ProfileDTO;
import com.victorfisyuk.messagesservice.integration.profilesservice.ProfilesServiceClient;
import org.mapstruct.Context;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(config = ModelMapperConfig.class)
public abstract class MessageMapper {
    @Autowired
    private ProfilesServiceClient profilesServiceClient;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senderId", expression = "java( getPrincipalId() )")
    @Mapping(target = "sentTimestamp", ignore = true)
    public abstract Message convertToModel(MessageComposeDTO messageComposeDTO);

    public MessageDTO convertToDTO(Message message) {
        Map<String, String> userNames = getUserNames(new HashSet<>(List.of(message.getSenderId(),
                message.getRecipientId())));
        return convertToDTO(message, userNames);
    }

    public List<MessageDTO> convertToDTO(List<Message> messages) {
        Set<String> userIds = Stream.concat(messages.stream().map(Message::getSenderId),
                        messages.stream().map(Message::getRecipientId))
                .collect(Collectors.toSet());
        Map<String, String> userNames = getUserNames(userIds);
        return convertToDTO(messages, userNames);
    }

    @Named("MessageWithContext")
    @Mapping(target = "senderName", source = "senderId", qualifiedByName = "UserName")
    @Mapping(target = "recipientName", source = "recipientId", qualifiedByName = "UserName")
    protected abstract MessageDTO convertToDTO(Message message, @Context Map<String, String> userNames);

    @IterableMapping(qualifiedByName = "MessageWithContext")
    protected abstract List<MessageDTO> convertToDTO(List<Message> messages, @Context Map<String, String> userNames);

    protected String getPrincipalId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof Jwt)) {
            throw new IllegalStateException("Unexpected principal object type: " + principal.getClass());
        }

        Jwt jwt = (Jwt) principal;
        return jwt.getSubject();
    }

    @Named("UserName")
    protected String getUserName(String userId, @Context Map<String, String> userNames) {
        return userNames.get(userId);
    }

    private Map<String, String> getUserNames(Set<String> userIds) {
        // TODO: Get all usernames in one call
        return userIds.stream()
                .map(profilesServiceClient::getProfile)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(ProfileDTO::getUserId, profile ->
                        String.format("%s %s", profile.getFirstName(), profile.getLastName())));
    }
}

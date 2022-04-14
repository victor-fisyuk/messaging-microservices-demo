package com.victorfisyuk.messagesservice.message;

import com.victorfisyuk.messagesservice.config.ModelMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

@Mapper(config = ModelMapperConfig.class)
public abstract class MessageMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senderId", expression = "java( getPrincipalId() )")
    @Mapping(target = "sentTimestamp", ignore = true)
    public abstract Message convertToModel(MessageComposeDTO messageComposeDTO);

    @Mapping(target = "senderName", ignore = true)
    @Mapping(target = "recipientName", ignore = true)
    public abstract MessageDTO convertToDTO(Message message);

    public abstract List<MessageDTO> convertToDTO(List<Message> messages);

    protected String getPrincipalId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof Jwt)) {
            throw new IllegalStateException("Unexpected principal object type: " + principal.getClass());
        }

        Jwt jwt = (Jwt) principal;
        return jwt.getSubject();
    }
}

package com.victorfisyuk.messagesservice.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MessageComposeDTO {
    @NotBlank
    @JsonProperty("recipient_id")
    private String recipientId;

    @NotBlank
    private String subject;

    @NotBlank
    private String text;
}

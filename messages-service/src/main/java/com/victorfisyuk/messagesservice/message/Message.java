package com.victorfisyuk.messagesservice.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@ToString
@Getter
@Setter
@SequenceGenerator(name = "message_sequence")
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_sequence")
    private Long id;

    @NotBlank
    @Column(nullable = false, columnDefinition="text")
    private String senderId;

    @NotBlank
    @Column(nullable = false, columnDefinition="text")
    private String recipientId;

    @NotBlank
    @Column(nullable = false, columnDefinition="text")
    private String subject;

    @NotBlank
    @Column(nullable = false, columnDefinition="text")
    private String text;

    @NotNull
    @Column(nullable = false, updatable = false)
    private Instant sentTimestamp;

    @PrePersist
    private void prePersist() {
        sentTimestamp = Instant.now();
    }
}

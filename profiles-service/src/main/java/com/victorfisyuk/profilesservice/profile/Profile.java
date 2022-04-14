package com.victorfisyuk.profilesservice.profile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;

@ToString
@Getter
@Setter
@SequenceGenerator(name = "profile_sequence")
@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profile_sequence")
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true, columnDefinition="text")
    private String userId;

    @NotBlank
    @Column(nullable = false, columnDefinition="text")
    private String firstName;

    @NotBlank
    @Column(nullable = false, columnDefinition="text")
    private String lastName;

    @NotBlank
    @Column(nullable = false, unique = true, columnDefinition="text")
    private String email;
}

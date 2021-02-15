package com.EGEA1R.CarService.persistance.entity;


import com.EGEA1R.CarService.validation.annotation.ValidEmail;

import com.EGEA1R.CarService.validation.annotation.ValidPassword;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "credential",
uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
public class Credential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    @Column(name = "credential_id")
    private Long credentialId;

    @NotNull
    @ValidEmail
    @Size(max = 255)
    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "password")
    @JsonIgnore
    @ValidPassword
    private String password;

    @Column(name = "secret")
    @JsonIgnore
    private String secret;

    @Column(name = "permission", columnDefinition = "VARCHAR(100) default 'ROLE_DISABLED'")
    private String permission;


    @Column(name = "multifactor_auth", columnDefinition = "VARCHAR(45) default 'NULL'")
    private String mfa;


    @OneToOne(cascade = CascadeType.REMOVE,
     fetch = FetchType.LAZY, mappedBy = "credential")
    @JsonIgnore
    private User user;


    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            mappedBy = "credential")
    private Set<PasswordResetToken> passwordResetToken = new HashSet<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            mappedBy = "credential")
    private Set<VerificationToken> verificationToken  = new HashSet<>();

}

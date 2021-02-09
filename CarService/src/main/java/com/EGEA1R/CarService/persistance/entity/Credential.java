package com.EGEA1R.CarService.persistance.entity;


import com.EGEA1R.CarService.validation.annotation.ValidEmail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.*;

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
    private Long credential_id;

    @NotNull
    @ValidEmail
    @Size(max = 255)
    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Column(name = "secret")
    private String secret;

    @Column(name = "permission", columnDefinition = "VARCHAR(100) default 'ROLE_DISABLED'")
    private String permission;

    @Pattern(regexp = "^{11}[0-9]")
    @Size(max = 11)
    @Column(name = "phone_number")
    private Integer phone_number;

    @Column(name = "multifactorauth", columnDefinition = "VARCHAR(45) default 'NULL'")
    private String mfa;
/*
    @OneToOne(cascade = CascadeType.REMOVE, mappedBy = "credential")
    private User user;
*/
}

package com.EGEA1R.CarService.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.mapping.Set;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "credential")
@EntityListeners(AuditingEntityListener.class)
public class Credential {

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    @Column(name = "credential_id")
    private Integer credential_id;

    @NotNull
    @Email
    @Size(max = 255)
    @Column(name = "e-mail")
    private String email;

    @NotNull
    @Size(min = 8)
    @Column(name = "password")
    private String password;

    @NotNull
    @PositiveOrZero
    @Column(name = "permission")
    private Integer permission;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "credential")
    private User user;
}

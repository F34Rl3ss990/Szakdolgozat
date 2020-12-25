package com.EGEA1R.CarService.entity;


import com.EGEA1R.CarService.validation.annotation.ValidEmail;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
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
    @Size(min = 8)
    @Column(name = "password")
    private String password;

    @Column(name = "permission", columnDefinition = "VARCHAR(100) default 'ROLE_DISABLED'")
    private String permission;
/*
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "credential")
    private User user;*/

}

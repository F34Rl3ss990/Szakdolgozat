package com.EGEA1R.CarService.persistance.entity;


import com.EGEA1R.CarService.validation.annotation.ValidAccentLetters;
import com.EGEA1R.CarService.validation.annotation.ValidEmail;

import com.EGEA1R.CarService.validation.annotation.ValidPassword;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "credential")
@DynamicInsert
@SqlResultSetMapping(
        name="FindByEmailMapping",
        classes = {
                @ConstructorResult(
                        targetClass = com.EGEA1R.CarService.persistance.entity.Credential.class,
                        columns = {
                                @ColumnResult(name="credential_id", type=Long.class),
                                @ColumnResult(name="email", type=String.class),
                                @ColumnResult(name="permission", type=String.class),
                                @ColumnResult(name="password", type=String.class)
                        }
                )
})
@SqlResultSetMapping(
        name="ExistByEmailMapping",
        classes = {
                @ConstructorResult(
                        targetClass = com.EGEA1R.CarService.persistance.entity.Credential.class,
                        columns = {
                                @ColumnResult(name="credential_id", type = Long.class)
                        }
                )
}
)
@SqlResultSetMapping(
        name="GetPermissionById",
        classes = {
                @ConstructorResult(
                        targetClass = com.EGEA1R.CarService.persistance.entity.Credential.class,
                        columns = {
                                @ColumnResult(name="permission", type = String.class)
                        }
                )
        }
)
@SqlResultSetMapping(
        name="GetEmailAndId",
        classes = {
                @ConstructorResult(
                        targetClass = com.EGEA1R.CarService.persistance.entity.Credential.class,
                        columns = {
                                @ColumnResult(name="email", type = String.class),
                                @ColumnResult(name="credential_id", type = Long.class)
                        }
                )
        }
)
@SqlResultSetMapping(
        name="GetPasswordAndId",
        classes = {
                @ConstructorResult(
                        targetClass = com.EGEA1R.CarService.persistance.entity.Credential.class,
                        columns = {
                                @ColumnResult(name="credential_id", type = Long.class),
                                @ColumnResult(name="password", type = String.class)
                        }
                )
        }
)
@SqlResultSetMapping(
        name="GetCredentialForMultiFactorAuth",
        classes = {
                @ConstructorResult(
                        targetClass = com.EGEA1R.CarService.persistance.entity.Credential.class,
                        columns = {
                                @ColumnResult(name="credential_id", type = Long.class),
                                @ColumnResult(name="email", type = String.class),
                                @ColumnResult(name="permission", type = String.class),
                                @ColumnResult(name="password", type = String.class),
                                @ColumnResult(name="multifactor_auth", type = String.class),
                                @ColumnResult(name="secret", type = String.class)

                        }
                )
        }
)
@SqlResultSetMapping(
        name="GetDetailsOfAdmins",
        classes = {
                @ConstructorResult(
                        targetClass = com.EGEA1R.CarService.persistance.entity.Credential.class,
                        columns = {
                                @ColumnResult(name="credential_id", type = Long.class),
                                @ColumnResult(name="email", type = String.class),
                                @ColumnResult(name="first_name", type = String.class),
                                @ColumnResult(name="last_name", type = String.class)
                        }
                )
        }
)

public class Credential{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    @Column(name = "credential_id", insertable = false, updatable = false)
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

    @Size(max = 60)
    @ValidAccentLetters
    @Column(name = "first_name")
    private String firstName;

    @Size(max = 60)
    @ValidAccentLetters
    @Column(name = "last_name")
    private String lastName;


    @OneToOne(cascade = CascadeType.REMOVE,
     fetch = FetchType.LAZY, mappedBy = "credential")
    @JsonIgnore
    private User user;


    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            mappedBy = "credential")
    private Set<PasswordReset> passwordReset = new HashSet<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            mappedBy = "credential")
    private Set<Verification> verification = new HashSet<>();

    public Credential(String email, Long credentialId){
        this.email = email;
        this.credentialId = credentialId;
    }

    public Credential(Long credentialId, String email, String permission, String password) {
        this.credentialId = credentialId;
        this.email = email;
        this.permission = permission;
        this.password = password;
    }

    public Credential(Long credentialId){
        this.credentialId = credentialId;
    }

    public Credential(String permission){
        this.permission = permission;
    }

    public Credential(Long credentialId, String password){
        this.credentialId = credentialId;
        this.password = password;
    }

    public Credential(Long credentialId, String email, String permission, String password, String mfa, String secret){
        this.credentialId = credentialId;
        this.email = email;
        this.permission = permission;
        this.password = password;
        this.mfa = mfa;
        this.secret = secret;
    }
}

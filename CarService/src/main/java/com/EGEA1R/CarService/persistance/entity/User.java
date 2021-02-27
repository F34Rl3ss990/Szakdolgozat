package com.EGEA1R.CarService.persistance.entity;

import com.EGEA1R.CarService.validation.annotation.ValidAccentLetters;
import com.EGEA1R.CarService.validation.annotation.ValidEmail;
import com.EGEA1R.CarService.validation.annotation.ValidPhoneNumber;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
@SqlResultSetMapping(
        name="GetUserByFkId",
        entities = {
                @EntityResult(entityClass = com.EGEA1R.CarService.persistance.entity.User.class)
                        }
)

@SqlResultSetMapping(
        name="GetUserByPermission",
        classes = {
                @ConstructorResult(
                        targetClass = com.EGEA1R.CarService.persistance.entity.User.class,
                        columns = {
                                @ColumnResult(name = "user_id", type = Long.class),
                                @ColumnResult(name = "first_name", type = String.class),
                                @ColumnResult(name = "last_name", type = String.class),
                                @ColumnResult(name = "e_mail", type = String.class),
                                @ColumnResult(name = "phone_number", type = String.class)
                        }
                )
        }
)
@SqlResultSetMapping(
        name = "GetUserDetailsByFkCarId",
        classes = {
                @ConstructorResult(
                        targetClass = com.EGEA1R.CarService.persistance.entity.User.class,
                        columns = {
                                @ColumnResult(name = "last_name", type = String.class),
                                @ColumnResult(name = "first_name", type = String.class),
                                @ColumnResult(name = "e_mail", type = String.class),
                                @ColumnResult(name = "phone_number", type = String.class)
                        }
                )
        }
)

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @NotNull
    @ValidAccentLetters
    @Size(max = 60)
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @ValidAccentLetters
    @Size(max = 60)
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @ValidEmail
    @Column(name = "e_mail")
    private String email;

    @NotNull
    @ValidPhoneNumber
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "fk_user_credential", insertable = false, updatable = false)
    private Long fkUserCredentialId;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "fk_user_credential")
    private Credential credential;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonIgnore
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.REMOVE,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<Car> car = new ArrayList<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "billingName", column =  @Column(name = "billing_name")),
            @AttributeOverride( name = "billingPhoneNumber", column = @Column(name = "billing_phone_number")),
            @AttributeOverride( name = "billingCountry", column = @Column(name = "billing_country")),
            @AttributeOverride( name = "billingZipCode", column = @Column(name = "billing_zip_code")),
            @AttributeOverride( name = "billingTown", column = @Column(name = "billing_town")),
            @AttributeOverride( name = "billingStreet", column = @Column(name = "billing_street")),
            @AttributeOverride( name = "billingOtherAddressType", column = @Column(name = "billing_other_address_type")),
            @AttributeOverride( name = "billingTaxNumber", column = @Column(name = "billing_tax_number")),
            @AttributeOverride( name = "billingEmail", column = @Column(name = "billing_email"))
    })
    private BillingInformation billingInformation;

    public User(Long userId, String firstName, String lastName, String email, String phoneNumber){
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public User(String lastName, String firstName, String email, String phoneNumber){
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}

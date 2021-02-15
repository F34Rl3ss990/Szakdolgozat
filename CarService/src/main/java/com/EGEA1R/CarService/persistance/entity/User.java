package com.EGEA1R.CarService.persistance.entity;

import com.EGEA1R.CarService.validation.annotation.ValidEmail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    @Column(name = "user_id")
    private Long userId;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z]+$")
    @Size(max = 60)
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z]+$")
    @Size(max = 40)
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @ValidEmail
    @Column(name = "e-mail")
    private String email;

    @NotNull
    @Pattern(regexp = "^[0-9]{11}$")
    @Size(max = 11)
    @Column(name = "phone_number")
    private Integer phoneNumber;

    @OneToOne
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
    private Set<Car> car = new HashSet<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "phoneNumber", column = @Column(name = "billing_phone_number")),
            @AttributeOverride( name = "country", column = @Column(name = "billing_country")),
            @AttributeOverride( name = "zipCode", column = @Column(name = "billing_zip_code")),
            @AttributeOverride( name = "town", column = @Column(name = "billing_town")),
            @AttributeOverride( name = "street", column = @Column(name = "billing_street")),
            @AttributeOverride( name = "otherAddressType", column = @Column(name = "billing_other_address_type")),
            @AttributeOverride( name = "taxNumber", column = @Column(name = "billing_tax_number")),
            @AttributeOverride( name = "email", column = @Column(name = "billing_email"))
    })
    private BillingInformation billingInformation;
}

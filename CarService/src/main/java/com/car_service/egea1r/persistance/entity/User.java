package com.car_service.egea1r.persistance.entity;

import com.car_service.egea1r.validation.annotation.ValidEmail;
import com.car_service.egea1r.validation.annotation.ValidName;
import com.car_service.egea1r.validation.annotation.ValidPhoneNumber;
import com.car_service.egea1r.web.data.DTO.UserDataDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
@SqlResultSetMapping(
        name = "GetUserByFkId",
        entities = {
                @EntityResult(entityClass = User.class)
        }
)

@SqlResultSetMapping(
        name = "GetUserByPermission",
        classes = {
                @ConstructorResult(
                        targetClass = User.class,
                        columns = {
                                @ColumnResult(name = "user_id", type = Long.class),
                                @ColumnResult(name = "name", type = String.class),
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
                        targetClass = UserDataDTO.class,
                        columns = {
                                @ColumnResult(name = "name", type = String.class),
                                @ColumnResult(name = "e_mail", type = String.class),
                                @ColumnResult(name = "phone_number", type = String.class),
                                @ColumnResult(name = "billing_name", type = String.class),
                                @ColumnResult(name = "billing_email", type = String.class),
                                @ColumnResult(name = "billing_phone_number", type = String.class),
                                @ColumnResult(name = "billing_zip_code", type = Integer.class),
                                @ColumnResult(name = "billing_town", type = String.class),
                                @ColumnResult(name = "billing_street", type = String.class),
                                @ColumnResult(name = "billing_other_address_type", type = String.class),
                                @ColumnResult(name = "billing_tax_number", type = String.class)
                        }
                )
        }
)

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @NotNull
    @ValidName
    @Size(max = 255)
    @Column(name = "name")
    private String name;

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

    @JsonIgnore
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.REMOVE,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @ToString.Exclude
    private List<Car> car = new ArrayList<>();

    @Embedded
    @AttributeOverride(name = "billingName", column = @Column(name = "billing_name"))
    @AttributeOverride(name = "billingPhoneNumber", column = @Column(name = "billing_phone_number"))
    @AttributeOverride(name = "billingZipCode", column = @Column(name = "billing_zip_code"))
    @AttributeOverride(name = "billingTown", column = @Column(name = "billing_town"))
    @AttributeOverride(name = "billingStreet", column = @Column(name = "billing_street"))
    @AttributeOverride(name = "billingOtherAddressType", column = @Column(name = "billing_other_address_type"))
    @AttributeOverride(name = "billingTaxNumber", column = @Column(name = "billing_tax_number"))
    @AttributeOverride(name = "billingEmail", column = @Column(name = "billing_email"))
    private BillingInformation billingInformation;

    public User(Long userId, String name, String email, String phoneNumber) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public User(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;

        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return 562048007;
    }
}



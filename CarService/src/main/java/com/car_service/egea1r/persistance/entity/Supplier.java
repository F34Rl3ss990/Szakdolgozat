package com.car_service.egea1r.persistance.entity;

import com.car_service.egea1r.validation.annotation.ValidEmail;
import com.car_service.egea1r.validation.annotation.ValidPhoneNumber;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "supplier")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id", insertable = false, updatable = false)
    private Long supplierId;

    @NotNull
    @Column(name = "name")
    private String name;

    @Pattern(regexp = "^[0-9]{8}[-][0-9]{8}[-][0-9]{8}$")
    @Column(name = "account_number")
    private String accountNumber;

    @NotNull
    @ValidEmail
    @Column(name = "email")
    private String email;

    @NotNull
    @ValidPhoneNumber
    @Column(name = "phone_number")
    private Integer phoneNumber;

    @NotNull
    @Column(name = "address")
    private String address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_supplier_order")
    private Order order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Supplier supplier = (Supplier) o;

        return Objects.equals(supplierId, supplier.supplierId);
    }

    @Override
    public int hashCode() {
        return 554628605;
    }
}

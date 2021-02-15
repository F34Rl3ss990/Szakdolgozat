package com.EGEA1R.CarService.persistance.entity;

import com.EGEA1R.CarService.validation.annotation.ValidEmail;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "supplier",
uniqueConstraints ={
        @UniqueConstraint(columnNames = "account_number"),
        @UniqueConstraint(columnNames = "e-mail")
})
@EntityListeners(AuditingEntityListener.class)
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    @Column(name = "supplier_id")
    private Long supplierId;

    @NotNull
    @Column(name = "name")
    private String name;

    @Pattern(regexp = "^[0-9]{8}[-][0-9]{8}[-][0-9]{8}$")
    @Column(name = "account_number")
    private String accountNumber;

    @NotNull
    @ValidEmail
    @Column(name = "e-mail")
    private String email;

    @NotNull
    @Pattern(regexp = "^[0-9]{11}$")
    @Size(max = 11)
    @Column(name = "phone_number")
    private Integer phoneNumber;

    @NotNull
    @Column(name = "address")
    private String address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_supplier_order")
    private Order order;
}

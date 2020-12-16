package com.EGEA1R.CarService.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "supplier")
@EntityListeners(AuditingEntityListener.class)
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Positive
    @Column(name = "supplier_id")
    private Integer supplier_id;

    @NotNull
    @Column(name = "name")
    private String name;

    @Pattern(regexp = "^{8}[0-9][-]{8}[0-9][-]{8}[0-9]$")
    @Column(name = "account_number")
    private String account_number;

    @NotNull
    @Email
    @Column(name = "e-mail")
    private String email;

    @NotNull
    @Pattern(regexp = "^{11}[0-9]")
    @Size(max = 11)
    @Column(name = "phone_number")
    private Integer phone_number;

    @NotNull
    @Column(name = "address")
    private String address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_supplier_order")
    private Order order;
}

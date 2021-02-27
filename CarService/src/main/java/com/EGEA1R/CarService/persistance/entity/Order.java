package com.EGEA1R.CarService.persistance.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    @Column(name = "order_id", insertable = false, updatable = false)
    private Long orderId;

    @NotNull
    @PastOrPresent
    @Column(name = "order_date")
    private LocalDate orderDate;

    @NotNull
    @PositiveOrZero
    @Column(name = "cost")
    private Integer cost;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "order")
    private Supplier supplier;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "order")
    private Finance finance;
}

package com.car_service.egea1r.persistance.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.Objects;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Order order = (Order) o;

        return Objects.equals(orderId, order.orderId);
    }

    @Override
    public int hashCode() {
        return 737800560;
    }
}

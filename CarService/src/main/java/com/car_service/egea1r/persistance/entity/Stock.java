package com.car_service.egea1r.persistance.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.util.Objects;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id", insertable = false, updatable = false)
    private Long stockId;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @PositiveOrZero
    @Pattern(regexp = "^[0-9]+$")
    @Column(name = "amount")
    private Integer amount;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_stock_item")
    private Item item;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Stock stock = (Stock) o;

        return Objects.equals(stockId, stock.stockId);
    }

    @Override
    public int hashCode() {
        return 1136165790;
    }
}

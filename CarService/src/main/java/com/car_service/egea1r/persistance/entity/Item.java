package com.car_service.egea1r.persistance.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.Objects;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", insertable = false, updatable = false)
    private Long itemId;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @PositiveOrZero
    @Pattern(regexp = "^[0-9]+$")
    @Column(name = "amount")
    private Integer amount;

    @NotNull
    @PositiveOrZero
    @Pattern(regexp = "^[0-9]+$")
    @Column(name = "cost")
    private Integer cost;

    @NotNull
    @PositiveOrZero
    @Pattern(regexp = "[0-3]")
    @Size(max = 1)
    @Column(name = "attached")
    private Integer attached;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "item")
    private Part part;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "item")
    private Stock stock;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Item item = (Item) o;

        return Objects.equals(itemId, item.itemId);
    }

    @Override
    public int hashCode() {
        return 1986846516;
    }
}

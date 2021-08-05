package com.car_service.egea1r.persistance.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "part")
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "part_id", insertable = false, updatable = false)
    private Long partId;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @PositiveOrZero
    @Pattern(regexp = "^[0-9]+$")
    @Column(name = "amount")
    private Integer amount;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_part_car")
    @ToString.Exclude
    private Car car;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_part_item")
    private Item item;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Part part = (Part) o;

        return Objects.equals(partId, part.partId);
    }

    @Override
    public int hashCode() {
        return 1725270524;
    }
}

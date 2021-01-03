package com.EGEA1R.CarService.persistance.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item")
@EntityListeners(AuditingEntityListener.class)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    @Column(name = "item_id")
    private Long item_id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @PositiveOrZero
    @Pattern(regexp = "[0-9]")
    @Column(name = "amount")
    private Integer amount;

    @NotNull
    @PositiveOrZero
    @Pattern(regexp = "[0-9]")
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
}

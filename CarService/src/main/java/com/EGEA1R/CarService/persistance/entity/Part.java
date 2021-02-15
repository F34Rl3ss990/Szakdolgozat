package com.EGEA1R.CarService.persistance.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "part")
@EntityListeners(AuditingEntityListener.class)
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    @Column(name = "part_id")
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
    private Car car;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_part_item")
    private Item item;

}

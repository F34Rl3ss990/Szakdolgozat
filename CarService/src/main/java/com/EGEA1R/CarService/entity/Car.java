package com.EGEA1R.CarService.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "car",
uniqueConstraints = {
        @UniqueConstraint(columnNames = "engine_number"),
        @UniqueConstraint(columnNames = "chassis_number")
})
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    @Column(name = "car_id", nullable = false)
    private Long car_id;

    @NotNull
    @Size(max = 30)
    @Pattern(regexp ="[a-zA-Z]")
    @Column(name = "brand")
    private String brand;

    @NotNull
    @Size(max = 30)
    @Column(name = "type")
    private String type;

    @NotNull
    @Size(max = 30)
    @Column(name = "engine_type")
    private String engine_type;

    @NotNull
    @Min(1900)
    @PastOrPresent
    @Positive
    @Column(name = "year_of_manufacture")
    private Integer year_of_manufacture;

    @Size(max = 30)
    @Pattern(regexp = "[A-Za-z0-9]")
    @Column(name = "engine_number")
    private String engine_number;

    @Size(max = 17)
    @Pattern(regexp = "[A-Za-z0-9]")
    @Column(name = "chassis_number")
    private String chassis_number;

    @Max(999999)
    @PositiveOrZero
    @Column(name = "mileage")
    private Integer mileage;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_car_user")
    private User user;
}

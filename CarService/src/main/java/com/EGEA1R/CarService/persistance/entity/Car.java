package com.EGEA1R.CarService.persistance.entity;

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
@Table(name = "car")
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    @Column(name = "car_id", nullable = false)
    private Long carId;

    @NotNull
    @Size(max = 30)
    @Pattern(regexp = "^[a-zA-Z]+$")
    @Column(name = "brand")
    private String brand;

    @NotNull
    @Size(max = 30)
    @Column(name = "type")
    private String type;

    @NotNull
    @Size(max = 30)
    @Column(name = "engine_type")
    private String engineType;

    @NotNull
    @Size(max = 10)
    @Column(name = "year_of_manufacture")
    private String yearOfManufacture;

    @Size(max = 30)
    @Pattern(regexp = "^[A-Za-z0-9]+$")
    @Column(name = "engine_number")
    private String engineNumber;

    @Size(max = 17)
    @Pattern(regexp = "^[A-Za-z0-9]+$")
    @Column(name = "chassis_number")
    private String chassisNumber;

    @Max(9999999)
    @PositiveOrZero
    @Column(name = "mileage")
    private Integer mileage;


    @Size(max = 10)
    @Column(name = "license_plate_number")
    private String licensePlateNumber;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_car_user")
    private User user;
}

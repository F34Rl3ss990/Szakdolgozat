package com.EGEA1R.CarService.persistance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "car")
@SqlResultSetMapping(
        name="GetCars",
        entities = {
                @EntityResult(entityClass = com.EGEA1R.CarService.persistance.entity.Car.class)
        }
)
@SqlResultSetMapping(
        name="GetCarByCarId",
        entities = {
                @EntityResult(entityClass = com.EGEA1R.CarService.persistance.entity.Car.class)
        }
)
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    @Column(name = "car_id", insertable = false, updatable = false)
    private Long carId;

    @NotNull
    @Column(name = "brand")
    private String brand;

    @NotNull
    @Column(name = "type")
    private String type;

    @NotNull
    @Column(name = "engine_type")
    private String engineType;

    @NotNull
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

    @Column(name = "license_plate_number")
    private String licensePlateNumber;

    @Column(name = "fk_car_user", updatable = false, insertable = false)
    private Long fkCarUserId;

    @ElementCollection
    @CollectionTable(name = "car_mileage", joinColumns = @JoinColumn(name = "fk_car_mileage_car"))
    @AttributeOverrides({
            @AttributeOverride(name = "mileage", column = @Column(name = "mileage")),
            @AttributeOverride(name = "dateOfSet", column = @Column(name = "date_of_set"))
    })
    private List<CarMileage> carMileages = new ArrayList<>();

    @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_car_user")
    private User user;

    public Car(String brand, String type, String yearOfManufacture, String engineType, String licensePlateNumber, Long carId){
        this.brand = brand;
        this.type = type;
        this.yearOfManufacture = yearOfManufacture;
        this.engineType = engineType;
        this.licensePlateNumber = licensePlateNumber;
        this.carId = carId;
    }
}

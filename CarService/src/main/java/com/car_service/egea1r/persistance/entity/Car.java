package com.car_service.egea1r.persistance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "car")
@SqlResultSetMapping(
        name="GetCars",
        entities = {
                @EntityResult(entityClass = Car.class)
        }
)
@SqlResultSetMapping(
        name="GetCarByCarId",
        entities = {
                @EntityResult(entityClass = Car.class)
        }
)
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Pattern(regexp = "^[A-Za-z0-9]{30}$")
    @Column(name = "engine_number")
    private String engineNumber;

    @Pattern(regexp = "^[A-Za-z0-9]{17}$")
    @Column(name = "chassis_number")
    private String chassisNumber;

    @Column(name = "license_plate_number")
    private String licensePlateNumber;

    @Column(name = "fk_car_user", updatable = false, insertable = false)
    private Long fkCarUserId;

    @ElementCollection
    @CollectionTable(name = "car_mileage", joinColumns = @JoinColumn(name = "fk_car_mileage_car"))
    @AttributeOverride(name = "mileage", column = @Column(name = "mileage"))
    @AttributeOverride(name = "dateOfSet", column = @Column(name = "date_of_set"))
    @ToString.Exclude
    private List<CarMileage> carMileages = new ArrayList<>();

    @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_car_user")
    @ToString.Exclude
    private User user;

    public Car(String brand, String type, String yearOfManufacture, String engineType, String licensePlateNumber, Long carId){
        this.brand = brand;
        this.type = type;
        this.yearOfManufacture = yearOfManufacture;
        this.engineType = engineType;
        this.licensePlateNumber = licensePlateNumber;
        this.carId = carId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Car car = (Car) o;

        return Objects.equals(carId, car.carId);
    }

    @Override
    public int hashCode() {
        return 2133657150;
    }
}

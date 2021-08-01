package com.car_service.egea1r.persistance.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "service_reservation")
@SqlResultSetMapping(
        name="GetServicesByUser",
        classes = {
                @ConstructorResult(
                        targetClass = ServiceReservation.class,
                        columns = {
                                @ColumnResult(name = "reserved_date", type = Date.class),
                                @ColumnResult(name = "brand", type = String.class),
                                @ColumnResult(name = "type", type = String.class)
                        }
                )
        }
)
@SqlResultSetMapping(
        name="GetServicesByToday",
        entities = {
                @EntityResult(entityClass = Car.class,
                        fields = {
                                @FieldResult(name = "brand", column = "brand"),
                                @FieldResult(name = "type", column = "type"),
                                @FieldResult(name = "engineType", column = "engine_type"),
                                @FieldResult(name = "yearOfManufacture", column = "year_of_manufacture")
                        }),
                @EntityResult(entityClass = User.class,
                        fields = {
                                @FieldResult(name = "firstName", column = "first_name"),
                                @FieldResult(name = "lastName", column = "last_name")
                        })
        }
)
public class ServiceReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_reservation_id", insertable = false, updatable = false)
    private Long serviceId;

    @NotNull
    @FutureOrPresent
    @Column(name = "reserved_date")
    private Date reservedDate;

    @NotNull
    @Column(name = "date_of_the_reservation", insertable = false, updatable = false)
    private LocalDate dateOfTheReservation;

    @Size(max = 1000)
    @Column(name = "comment")
    private String comment;

    @Size(max = 1000)
    @Column(name = "reserved_services")
    private String reservedServices;

    @Column(name = "fk_service_reservation_car", insertable = false, updatable = false)
    private Long fkServiceReservationCarId;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_service_reservation_car")
    private Car car;

    public ServiceReservation(Date reservedDate, String brand, String type){
        this.reservedDate = reservedDate;
        this.car = Car.builder()
                .brand(brand)
                .type(type)
                .build();
    }
}

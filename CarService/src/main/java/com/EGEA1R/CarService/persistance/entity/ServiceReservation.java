package com.EGEA1R.CarService.persistance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "service_reservation")
@EntityListeners(AuditingEntityListener.class)
@SqlResultSetMapping(
        name="GetServicesByUser",
        classes = {
                @ConstructorResult(
                        targetClass = com.EGEA1R.CarService.persistance.entity.ServiceReservation.class,
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
                @EntityResult(entityClass = com.EGEA1R.CarService.persistance.entity.Car.class,
                        fields = {
                                @FieldResult(name = "brand", column = "brand"),
                                @FieldResult(name = "type", column = "type"),
                                @FieldResult(name = "engineType", column = "engine_type"),
                                @FieldResult(name = "yearOfManufacture", column = "year_of_manufacture")
                        }),
                @EntityResult(entityClass = com.EGEA1R.CarService.persistance.entity.User.class,
                        fields = {
                                @FieldResult(name = "firstName", column = "first_name"),
                                @FieldResult(name = "lastName", column = "last_name")
                        })
        }
)
public class ServiceReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
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
package com.EGEA1R.CarService.persistance.entity;

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
public class ServiceReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    @Column(name = "service_reservation_id")
    private Long serviceId;

    @NotNull
    @FutureOrPresent
    @Column(name = "reserved_date")
    private LocalDate reservedDate;

    @NotNull
    @CreationTimestamp
    @Column(name = "date_of_the_reservation")
    private LocalDate dateOfTheReservation;

    @Size(max = 1000)
    @Column(name = "comment")
    private String comment;

    @Size(max = 1000)
    @Column(name = "reserved_services")
    private String reservedServices;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_service_reservation_car")
    private Car car;

}

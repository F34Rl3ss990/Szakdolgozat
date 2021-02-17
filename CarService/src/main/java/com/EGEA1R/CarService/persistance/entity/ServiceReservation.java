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
import java.util.Date;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "service")
@EntityListeners(AuditingEntityListener.class)
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    @Column(name = "service_id")
    private Long serviceId;

    @NotNull
    @FutureOrPresent
    @Column(name = "service_date")
    private Date serviceDate;

    @NotNull
    @CreationTimestamp
    @Column(name = "reservation_date")
    private Date reservationDate;

    @Size(max = 1000)
    @Column(name = "comment")
    private String comment;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_service_car")
    private Car car;

    @ElementCollection
    @CollectionTable(name = "services",
    joinColumns = @JoinColumn(name = "fk_services_service")
    )
    private List<String> services;

}

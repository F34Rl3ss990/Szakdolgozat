package com.EGEA1R.CarService.persistance.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "workday")
@EntityListeners(AuditingEntityListener.class)
public class Workday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    @Column(name = "workday_id")
    private Long workdayId;

    @NotNull
   // @CreationTimestamp
    @Column(name = "workday_date")
    private LocalDate workdayDate;

    @NotNull
    @Size(max = 30)
    //@Pattern(regexp = "^[a-zA-Z]+$")
    @Column(name = "is_worked")
    private String isWorked;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_workday_employee")
    private Employee employee;

}

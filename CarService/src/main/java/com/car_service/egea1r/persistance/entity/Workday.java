package com.car_service.egea1r.persistance.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;


@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "workday")
public class Workday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workday_id", insertable = false, updatable = false)
    private Long workdayId;

    @NotNull
    @Column(name = "workday_date")
    private LocalDate workdayDate;

    @NotNull
    @Size(max = 30)
    @Column(name = "is_worked")
    private String isWorked;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_workday_employee")
    @ToString.Exclude
    private Employee employee;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Workday workday = (Workday) o;

        return Objects.equals(workdayId, workday.workdayId);
    }

    @Override
    public int hashCode() {
        return 1088180920;
    }
}

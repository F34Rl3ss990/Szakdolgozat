package com.car_service.egea1r.persistance.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Objects;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee",
uniqueConstraints = {
        @UniqueConstraint(columnNames = "account_number")
})
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id", insertable = false, updatable = false)
    private Long employeeId;

    @NotNull
    @Size(max = 60)
    @Pattern(regexp = "^[a-zA-Z]+$")
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Size(max = 40)
    @Pattern(regexp = "^[a-zA-Z]+$")
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Size(max = 26)
    @Pattern(regexp = "^[0-9]{8}[-][0-9]{8}[-][0-9]{8}$")
    @Column(name = "account_number")
    private String accountNumber;

    @NotNull
    @PastOrPresent
    @Column(name = "date_of_join")
    private LocalDate dateOfJoin;

    @NotNull
    @Positive
    @Column(name = "salary_per_hour")
    private Integer salaryPerHour;

    @NotNull
    @PositiveOrZero
    @Column(name = "day_offs")
    private Integer dayOffs;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Employee employee = (Employee) o;

        return Objects.equals(employeeId, employee.employeeId);
    }

    @Override
    public int hashCode() {
        return 949447908;
    }
}

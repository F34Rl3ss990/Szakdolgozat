package com.EGEA1R.CarService.persistance.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee",
uniqueConstraints = {
        @UniqueConstraint(columnNames = "account_number")
})
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
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
}

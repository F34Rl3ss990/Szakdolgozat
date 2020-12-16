package com.EGEA1R.CarService.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee")
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Positive
    @Column(name = "employee_id")
    private Integer employee_id;

    @NotNull
    @Size(max = 60)
    @Pattern(regexp = "[a-zA-Z]")
    @Column(name = "first_name")
    private String first_name;

    @NotNull
    @Size(max = 40)
    @Pattern(regexp = "[a-zA-Z]")
    @Column(name = "last_name")
    private String last_name;

    @NotNull
    @Size(max = 26)
    @Pattern(regexp = "^{8}[0-9][-]{8}[0-9][-]{8}[0-9]$")
    @Column(name = "account_number")
    private String account_number;

    @NotNull
    @PastOrPresent
    @Column(name = "date_of_join")
    private Date date_of_join;

    @NotNull
    @Positive
    @Column(name = "salary_per_hour")
    private Integer salary_per_hour;

    @NotNull
    @PositiveOrZero
    @Column(name = "day_offs")
    private Integer day_offs;
}

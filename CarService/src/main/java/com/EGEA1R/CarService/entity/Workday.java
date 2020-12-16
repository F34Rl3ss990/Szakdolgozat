package com.EGEA1R.CarService.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
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
    @NotNull
    @Positive
    @Column(name = "workday_id")
    private Integer workday_id;

    @NotNull
    @CreationTimestamp
    @Column(name = "workday_date")
    private Date workday_date;

    @NotNull
    @Size(max = 30)
    @Pattern(regexp = "[a-zA-Z]")
    @Column(name = "is_worked")
    private String is_worked;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_workday_employee")
    private Employee employee;

}

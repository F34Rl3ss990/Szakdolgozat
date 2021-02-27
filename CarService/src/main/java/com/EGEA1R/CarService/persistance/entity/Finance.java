package com.EGEA1R.CarService.persistance.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "finance")
public class Finance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    @Column(name = "finance_id", insertable = false, updatable = false)
    private Long financeId;

    @NotNull
    @CreationTimestamp
    @Column(name = "payday")
    private LocalDate payday;

    @NotNull
    @Pattern(regexp = "^[-][0-9]+$")
    @Column(name = "amount")
    private Integer amount;


    @Size(max = 80)
    @Pattern(regexp = "[\\sa-zA-Z]")
    @Column(name = "account_name")
    private String accountName;

    @Size(max = 26)
    @Pattern(regexp = "^[0-9]{8}[-][0-9]{8}[-][0-9]{8}$")
    @Column(name = "account_number")
    private String accountNumber;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_finance_employee")
    private Employee employee;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_finance_order")
    private Order order;

}

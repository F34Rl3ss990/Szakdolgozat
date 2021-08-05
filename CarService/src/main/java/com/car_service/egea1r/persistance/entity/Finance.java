package com.car_service.egea1r.persistance.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
@Table(name = "finance")
public class Finance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "finance_id", insertable = false, updatable = false)
    private Long financeId;

    @CreationTimestamp
    @Column(name = "payday")
    private LocalDate payday;

    @NotNull
    @Pattern(regexp = "^[-][0-9]+$")
    @Column(name = "amount")
    private String amount;


    @Size(max = 80)
    @Pattern(regexp = "[\\sa-zA-Z]")
    @Column(name = "account_name")
    private String accountName;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "is_paid")
    private Boolean isPaid;

    @Size(max = 26)
    @Pattern(regexp = "^[0-9]{8}[-][0-9]{8}[-][0-9]{8}$")
    @Column(name = "account_number")
    private String accountNumber;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_finance_employee")
    @ToString.Exclude
    private Employee employee;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_finance_order")
    private Order order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Finance finance = (Finance) o;

        return Objects.equals(financeId, finance.financeId);
    }

    @Override
    public int hashCode() {
        return 201713355;
    }
}

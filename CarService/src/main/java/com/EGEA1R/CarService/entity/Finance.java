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
@Table(name = "finance",
uniqueConstraints = {
        @UniqueConstraint(columnNames = "account_number")
})
@EntityListeners(AuditingEntityListener.class)
public class Finance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    @Column(name = "finance_id")
    private Long finance_id;

    @NotNull
    @CreationTimestamp
    @Column(name = "payday")
    private Date payday;

    @NotNull
    @Pattern(regexp = "[-][0-9]")
    @Column(name = "amount")
    private Integer amount;


    @Size(max = 80)
    @Pattern(regexp = "[\\sa-zA-Z]")
    @Column(name = "account_name")
    private String account_name;

    @Size(max = 26)
    @Pattern(regexp = "^{8}[0-9][-]{8}[0-9][-]{8}[0-9]$")
    @Column(name = "account_number")
    private String account_number;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_finance_employee")
    private Employee employee;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_finance_order")
    private Order order;

}

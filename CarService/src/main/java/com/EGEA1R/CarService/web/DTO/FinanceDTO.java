package com.EGEA1R.CarService.web.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinanceDTO {

    @CreationTimestamp
    private LocalDate payday;

    @NotNull
    @Pattern(regexp = "^[-][0-9]+$")
    private String amount;

    @Size(max = 80)
    @Pattern(regexp = "[\\sa-zA-Z]")
    private String accountName;

    private String paymentMethod;

    private Boolean isPaid;

    @Size(max = 26)
    @Pattern(regexp = "^[0-9]{8}[-][0-9]{8}[-][0-9]{8}$")
    private String accountNumber;


}

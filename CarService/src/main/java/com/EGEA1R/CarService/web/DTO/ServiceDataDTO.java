package com.EGEA1R.CarService.web.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.SqlResultSetMapping;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDataDTO {

    private String brand;

    private String type;

    private String licensePlateNumber;

    @NotNull
    private Date date;

    @NotNull
    private String mileage;

    @NotNull
    private String billNum;

    @NotNull
    private String servicesDone;

    private String comment;

    @NotNull
    private String amount;

    @NotNull
    private Long fkCarId;

    public ServiceDataDTO(Date date, String mileage, String billNum, String servicesDone, String comment, String amount, Long fkCarId){
        this.date = date;
        this.mileage = mileage;
        this.billNum = billNum;
        this.servicesDone = servicesDone;
        this.comment = comment;
        this.amount = amount;
        this.fkCarId = fkCarId;
    }


}


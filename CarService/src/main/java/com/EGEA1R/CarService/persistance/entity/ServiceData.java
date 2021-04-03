package com.EGEA1R.CarService.persistance.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "service_data")
@SqlResultSetMapping(
        name="GetServiceDataByUser",
        classes = {
                @ConstructorResult(
                        targetClass = com.EGEA1R.CarService.web.DTO.ServiceDataDTO.class,
                        columns = {
                                @ColumnResult(name = "brand", type = String.class),
                                @ColumnResult(name = "type", type = String.class),
                                @ColumnResult(name = "license_plate_number", type = String.class),
                                @ColumnResult(name = "date", type = Date.class),
                                @ColumnResult(name = "mileage", type = String.class),
                                @ColumnResult(name = "bill_num", type = String.class),
                                @ColumnResult(name = "services_done", type = String.class),
                                @ColumnResult(name = "comment", type = String.class),
                                @ColumnResult(name = "amount", type = String.class),
                                @ColumnResult(name = "car_id", type = Long.class)
                        }
                )
        }
)
@SqlResultSetMapping(
        name="GetServiceData",
        classes = {
                @ConstructorResult(
                        targetClass = com.EGEA1R.CarService.web.DTO.ServiceDataDTO.class,
                        columns = {
                                @ColumnResult(name = "date", type = Date.class),
                                @ColumnResult(name = "mileage", type = String.class),
                                @ColumnResult(name = "bill_num", type = String.class),
                                @ColumnResult(name = "services_done", type = String.class),
                                @ColumnResult(name = "comment", type = String.class),
                                @ColumnResult(name = "amount", type = String.class),
                                @ColumnResult(name = "car_id", type = Long.class)
                        }
                )
        }
)
public class ServiceData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_data_id", insertable = false, updatable = false)
    private Long serviceDataId;

    @NotNull
    @Column(name = "date")
    private Date date;

    @NotNull
    @Column(name = "bill_num")
    private String billNum;

    @NotNull
    @Column(name = "services_done")
    private String servicesDone;

    @Column(name = "comment")
    private String comment;

    @NotNull
    @Column(name = "mileage")
    private String mileage;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "fk_service_data_finance")
    private Finance finance;
}

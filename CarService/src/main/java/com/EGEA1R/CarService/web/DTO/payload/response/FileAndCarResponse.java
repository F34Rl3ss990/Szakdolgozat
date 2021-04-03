package com.EGEA1R.CarService.web.DTO.payload.response;

import com.EGEA1R.CarService.persistance.entity.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileAndCarResponse {

    private Date date;
    private String mileage;
    private Long carId;
    private String brand;
    private String type;
    private String licensePlateNumber;
    private List<Long> documentIds;
    private Long serviceDataId;
    private List<ResponseFile> documentList;
}

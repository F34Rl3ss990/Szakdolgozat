package com.car_service.egea1r.web.data.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileAndCarResponse that = (FileAndCarResponse) o;
        return getCarId().equals(that.getCarId()) && Objects.equals(getLicensePlateNumber(), that.getLicensePlateNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCarId(), getLicensePlateNumber());
    }
}

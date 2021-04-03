package com.EGEA1R.CarService.web.DTO.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRequest {

    @NotNull
    private String fileType;

    @NotNull
    private String name;

    @Lob
    @NotNull
    private byte[] data;

    private String size;

    @NotNull
    private Long fkDocumentCarId;

    @NotNull
    private Long fkDocumentServiceData;
}

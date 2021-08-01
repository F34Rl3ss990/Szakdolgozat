package com.car_service.egea1r.web.data.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRequest {

    @NotBlank
    private String fileType;

    @NotBlank
    private String name;

    @NotNull
    private byte[] data;

    private String size;

    @NotEmpty
    private Long fkDocumentCarId;

    @NotEmpty
    private Long fkDocumentServiceData;
}

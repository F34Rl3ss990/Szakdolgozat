package com.EGEA1R.CarService.web.DTO;

import lombok.Builder;

import java.time.LocalDate;
import java.util.Date;

@Builder
public class DocumentDTO {

    private Integer file_id;

    private String type;

    private LocalDate upload_time;

    private byte[] data;
}

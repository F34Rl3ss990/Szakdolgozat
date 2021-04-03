package com.EGEA1R.CarService.web.DTO.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseFile {
    private String name;
    private Long id;
    private String type;
    private String size;
}

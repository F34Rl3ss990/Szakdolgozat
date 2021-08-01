package com.car_service.egea1r.web.data.payload.response;

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

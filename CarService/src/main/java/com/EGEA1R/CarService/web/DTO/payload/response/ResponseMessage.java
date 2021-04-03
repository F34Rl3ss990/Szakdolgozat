package com.EGEA1R.CarService.web.DTO.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ResponseMessage {
    private List<String> message;
}

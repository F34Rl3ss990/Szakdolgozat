package com.EGEA1R.CarService.web.DTO.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String tokenType = "Bearer";
    private Long id;
    private String email;
    private String roles;
    private Long date;

    public JwtResponse(String accessToken, Long id, String email, String roles) {
        this.id = id;
        this.email = email;
        this.roles = roles;
        this.token = accessToken;
        this.date = (new Date((new Date()).getTime() + 86400000)).getTime();
    }
}

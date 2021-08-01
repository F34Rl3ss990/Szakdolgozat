package com.car_service.egea1r.service.interfaces;

public interface TotpManager {

    String generateSecret();

    String getUriForImage(String secret);

    boolean verifyCode(String code, String secret);
}

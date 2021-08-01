package com.car_service.egea1r.service.interfaces;

public interface OTPService {
    int generateOTP(String key);

    int getOtp(String key);

    void clearOTP(String key);
}

package com.EGEA1R.CarService.service.interfaces;

public interface OTPService {
    int generateOTP(String key);

    int getOtp(String key);

    void clearOTP(String key);
}

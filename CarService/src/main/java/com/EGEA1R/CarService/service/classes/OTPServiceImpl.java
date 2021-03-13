package com.EGEA1R.CarService.service.classes;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.EGEA1R.CarService.service.interfaces.OTPService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

@Service
public class OTPServiceImpl implements OTPService {

    private final Random rand = SecureRandom.getInstanceStrong();
    private static final Integer EXPIRE_MINS = 4;
    private final LoadingCache<String, Integer> otpCache;
    public OTPServiceImpl() throws NoSuchAlgorithmException {
        super();
        otpCache = CacheBuilder.newBuilder().
                expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Integer>() {
                    public Integer load(@NotNull String key) {
                        return 0;
                    }
                });
    }

    @Override
    public int generateOTP(String key){
        int otp = 100000 + rand.nextInt(900000);
        otpCache.put(key, otp);
        return otp;
    }

    @Override
    public int getOtp(String key){
        try{
            return otpCache.get(key);
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public void clearOTP(String key){
        otpCache.invalidate(key);
    }
}

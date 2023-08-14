package com.example.smsrly.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OTPService {

    private static final int EXPIRE_MIN = 10;
    private final LoadingCache<String, Integer> OTPCache;
    private final Random random = new Random();

    public OTPService() {
        super();
        OTPCache = CacheBuilder.newBuilder()
                .expireAfterWrite(EXPIRE_MIN, TimeUnit.MINUTES)
                .maximumSize(1000000)
                .build(new CacheLoader<>() {

                    @Override
                    public Integer load(String s) {
                        return 0;
                    }
                });
    }

    public Integer generateOTP(String key) {
        int OTP = random.nextInt(9000) + 1000;
        OTPCache.put(key, OTP);
        System.out.println("Generated OTP for UserName: " + key + ", OTP code: " + OTP);
        return OTP;
    }

    public Integer getOTPByKey(String key) {
        return OTPCache.getIfPresent(key);
    }

    public void clearOTPFromCache(String key) {
        OTPCache.invalidate(key);
    }

    public Boolean isValidateOTP(String key, Integer otpNumber) {
        // get OTP from cache
        Integer cacheOTP = getOTPByKey(key);
        if (cacheOTP != null && cacheOTP.equals(otpNumber)) {
            clearOTPFromCache(key);
            return true;
        }
        return false;
    }

    public Boolean isOTPCodeExists(String key) {
        return getOTPByKey(key) != null;
    }

}

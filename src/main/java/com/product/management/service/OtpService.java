package com.product.management.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {
    private Map<String, String> otpStore = new ConcurrentHashMap<>();

    public String generateOtp(String key) {
        String otp = String.valueOf(new Random().nextInt(899999) + 100000);
        otpStore.put(key, otp);
        return otp;
    }

    public boolean validateOtp(String key, String otp) {
        return otp.equals(otpStore.get(key));
    }
}

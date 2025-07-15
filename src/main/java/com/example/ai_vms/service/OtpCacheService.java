package com.example.ai_vms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class OtpCacheService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // Store OTP with an expiry time of 5 minutes
    public void saveOtp(String key, String otp) {
        redisTemplate.opsForValue().set(key, otp, 5, TimeUnit.MINUTES);
    }

    // Fetch OTP from the cache
    public String getOtp(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}

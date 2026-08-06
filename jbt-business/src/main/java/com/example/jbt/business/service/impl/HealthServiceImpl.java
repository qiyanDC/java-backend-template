package com.example.jbt.business.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.data.redis.RedisHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Service;

import com.example.jbt.business.service.HealthService;

@Service
public class HealthServiceImpl implements HealthService {

    @Autowired
    private RedisHealthIndicator redisHealthIndicator;

    @Override
    public Health checkStatus() {
        return redisHealthIndicator.health();
    }
}
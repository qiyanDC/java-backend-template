package com.example.jbt.business.service;

import org.springframework.boot.actuate.health.Health;

public interface HealthService {

    Health checkStatus();
}
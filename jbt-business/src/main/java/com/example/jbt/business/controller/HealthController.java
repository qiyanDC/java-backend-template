package com.example.jbt.business.controller;

import org.springframework.boot.actuate.health.Health;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jbt.business.service.HealthService;
import com.example.jbt.common.result.R;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("/health")
public class HealthController {

    @Resource
    private HealthService healthService;

    @GetMapping("/health-check")
    public R<Health> healthCheck() {
        return R.success(healthService.checkStatus());
    }
}
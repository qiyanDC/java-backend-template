package com.example.jbt.service;

import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

import com.example.jbt.common.utils.JsonUtils;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}, scanBasePackages = "com.example.jbt")
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);
    
    public static void main(String[] args) {
        final SpringApplication application = new SpringApplication(Application.class);
        application.addListeners(
            (ApplicationListener<ApplicationEnvironmentPreparedEvent>) event -> {
                final Properties properties = System.getProperties();
                final SortedMap<String, Object> map = new TreeMap<>();
                properties.forEach((key, value) -> map.put(JsonUtils.toJson(key), value));
                log.info("系统环境变量: {}", JsonUtils.toJson(map));
            }
        );
        application.run(args);
    }
}
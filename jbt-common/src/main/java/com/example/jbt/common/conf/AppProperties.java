package com.example.jbt.common.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.example.jbt.common.utils.BeanUtils;
import com.example.jbt.common.utils.HostUtils;
import com.example.jbt.common.utils.ObjectUtils;

@Component
@ConfigurationProperties("spring.app")
public class AppProperties {

    private static volatile AppProperties INSTANCE = null;

    private AppProperties() {}

    public static AppProperties instance() {
        if(ObjectUtils.isEmpty(INSTANCE)) {
            synchronized (AppProperties.INSTANCE) {
                if(ObjectUtils.isEmpty(INSTANCE)) {
                    INSTANCE = BeanUtils.getBean(AppProperties.class);
                }
            }
        }
        return INSTANCE;
    }

    private String name;

    private String version;

    private String env;

    private String ip = HostUtils.getHostName();

    private String port;

    public static AppProperties getINSTANCE() {
        return INSTANCE;
    }

    public static void setINSTANCE(AppProperties iNSTANCE) {
        INSTANCE = iNSTANCE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
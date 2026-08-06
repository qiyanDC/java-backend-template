package com.example.jbt.common.conf.init;

import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.jbt.common.conf.AppProperties;
import com.example.jbt.common.utils.BeanUtils;

import jakarta.annotation.Resource;

@Component
@Order(-1)
public class ProjectInit implements ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(ProjectInit.class);

    @Resource
    private AppProperties appProperties;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        BeanUtils.setSpringContext(applicationContext);
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ConditionalOnExpression("'local'.equals('${spring.app.env}')")
    @Bean
    public CommandLineRunner printBeanCommandLineRunner(ApplicationContext context) {
        return args -> {
            log.info("打印所有bean:");
            Stream.of(context.getBeanDefinitionNames()).sorted().forEach(System.out::println);
        };
    }

    @Bean
    public CommandLineRunner beanInitPrintCommandLineRunner(ApplicationContext context) {
        return args -> {
            final StringBuilder config = new StringBuilder()
                    .append(String.format("%s【%s】 : %s\n", "spring.application.name", "应用名称", appProperties.getName()))
                    .append(String.format("%s【%s】 : %s\n", "spring.application.version", "当前运行版本号", appProperties.getVersion()))
                    .append(String.format("%s【%s】 : %s\n", "spring.app.env", "当前环境：[dev:本地|uat:UAT测试|beta:测试|prod:生产]", appProperties.getEnv()))
                    .append(String.format("%s【%s】 : %s\n", "spring.app.ip", "当前主机 IP 地址", appProperties.getIp()))
                    .append(String.format("%s【%s】 : %s", "server.port", "当前服务端口", appProperties.getPort()));
            log.info("\n┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬ 环境配置 ┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬\n{}\n┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴ 环境配置 ┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴", config.toString());
        };
    }
}
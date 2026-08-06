package com.example.jbt.common.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

public final class BeanUtils {
    private BeanUtils() {}

    private static ApplicationContext SPRING_CONTEXT;

    private static final Map<String, Object> BEAN_MAP = new ConcurrentHashMap<>();

    public static void setSpringContext(ApplicationContext applicationContext) {
        SPRING_CONTEXT = applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> beanClass) {
        if(ObjectUtils.isNotEmpty(SPRING_CONTEXT)) {
            try {
                if(!BEAN_MAP.containsKey(beanClass.getName())) {
                    // 缓存从 spring application context 获取到的bean，避免每次通过反射获取
                    BEAN_MAP.put(beanClass.getName(), SPRING_CONTEXT.getBean(beanClass));
                }
                return (T) BEAN_MAP.get(beanClass.getName());
            } catch(NoSuchBeanDefinitionException e) {
                
            }
        }
        return null;
    }
}
package com.example.jbt.common.conf.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.example.jbt.common.utils.StringUtils;
import com.example.jbt.common.utils.TraceIdUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TraceIdInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String traceId = request.getHeader(TraceIdUtils.TRACE_ID);
        if (StringUtils.isBlank(traceId)) {
            traceId = TraceIdUtils.generateTraceId();
        }
        TraceIdUtils.setTraceId(traceId);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        TraceIdUtils.removeTraceId();
    }
}
package com.example.jbt.common.aop.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.example.jbt.common.enums.HttpStatusEnums;
import com.example.jbt.common.exception.BusinessException;
import com.example.jbt.common.result.R;

import jakarta.servlet.Servlet;

@RestControllerAdvice
@ConditionalOnClass({Servlet.class})
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //处理未知错误
    @ExceptionHandler(Exception.class)
    public R<?> handleUnknownException(Exception e) {
        log.error("异常栈:", e);
        if(e instanceof MaxUploadSizeExceededException){
            return R.error(HttpStatusEnums.ERROR.getCode(), "上传文件大小超出限制");
        }
        return R.error(HttpStatusEnums.ERROR.getCode(), HttpStatusEnums.ERROR.getDesc());
    }

    // 处理业务异常
    @ExceptionHandler(BusinessException.class)
    public R<?> handleBusinessException(BusinessException e) {
        log.error("异常栈:", e);
        return R.error(e.getCode(), e.getMessage());
    }

    // 处理参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        //默认只返给前端第一个报错
        return R.error(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }
}
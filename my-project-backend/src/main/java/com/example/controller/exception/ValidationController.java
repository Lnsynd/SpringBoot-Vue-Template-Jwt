package com.example.controller.exception;

import com.example.entity.RestBean;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created by 刘千山 on 2023/8/11/011-17:10
 */
@Slf4j
@RestControllerAdvice
public class ValidationController {

    @ExceptionHandler(value = ValidationException.class)
    public RestBean<Void> validateException(ValidationException e){
        log.warn("Resolve[{},{}]",e.getClass().getName(),e.getMessage());
        return RestBean.failure(400,"请求参数有误");
    }
}

package com.hutoto.paidaxing.config.exception;

import com.hutoto.paidaxing.commons.Result;
import com.hutoto.paidaxing.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局统一异常处理配置
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result nullPointerException(NullPointerException e) {
        log.error(e.getMessage(), e);
        return Result.error("操作失败,存在空指针");
    }

    /**
     * 算数异常
     */
    @ExceptionHandler(ArithmeticException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result arithmeticException(ArithmeticException e) {
        log.error(e.getMessage(), e);
        return Result.error("操作失败,存在异常的运算条件");
    }

    /**
     * 类型转换异常
     */
    @ExceptionHandler(ClassCastException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result classCastException(ClassCastException e) {
        log.error(e.getMessage(), e);
        return Result.error("操作失败,存在异常的类型转换");
    }

    /**
     * post请求参数校验异常
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.OK)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder sb = new StringBuilder("校验失败:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getDefaultMessage()).append(", ");
        }
        String msg = sb.toString();
        msg = ("".equals(msg) ? "" : msg.substring(0, msg.length() - 1));
        return Result.error(msg);
    }

    /**
     * Exception异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Result exception(Exception e) {
        log.error(e.getMessage(), e);
        return Result.error();
    }

    /**
     * bizException异常
     */
    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result bizException(BizException e) {
        log.error(e.getMessage(), e);
        return Result.error(e.getMessage());
    }
}

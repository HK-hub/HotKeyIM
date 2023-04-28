package com.hk.im.admin.advice;


import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.hk.im.common.error.ApiException;
import com.hk.im.common.error.BaseException;
import com.hk.im.common.error.BusinessException;
import com.hk.im.common.error.CommonException;
import com.hk.im.common.resp.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class WebExceptionAdvice {


    // 自定义异常
    @ExceptionHandler(BaseException.class)
    public BaseException handleBaseException(BaseException exception){
        // 日志记录，链路追踪
        log.error("base exception:",exception);

        return exception;
    }

    /**
     * 处理token异常
     */
    @ExceptionHandler({SignatureVerificationException.class, AlgorithmMismatchException.class, JWTDecodeException.class})
    public BaseException tokenErrorException() {
        BaseException exception = new ApiException(ResultCode.TOKEN_ERROR);
        return exception;
    }

    /**
     * 处理token异常
     */
    @ResponseBody
    @ExceptionHandler({TokenExpiredException.class})
    public Exception tokenExpiredException() {
        BaseException exception = new ApiException(ResultCode.UNAUTHORIZED);
        return exception;
    }


    // 运行时异常
    @ExceptionHandler(RuntimeException.class)
    public BaseException handleRuntimeException(RuntimeException e) {
        // 构造异常响应对象
        BaseException exceptionResult = new CommonException(ResultCode.SERVER_BUSY);
        // exceptionResult.setExceptionObject(e);
        exceptionResult.setMessage(e.getMessage());
        exceptionResult.setCauses(Objects.toString(e.getCause(),"unknown error"));

        // 日志记录，链路追踪
        log.error("runtime exception:",e);

        return exceptionResult;
    }


    // 全部异常
    @ExceptionHandler(Exception.class)
    public BaseException handleException(Exception e){

        // 构造异常响应对象
        BaseException exceptionResult = new BusinessException(ResultCode.SERVER_BUSY);
        // exceptionResult.setExceptionObject(e);
        exceptionResult.setMessage(e.getMessage());
        exceptionResult.setCauses(Objects.toString(e.getCause()));

        // 日志记录，链路追踪
        log.error("common exception:",e);

        return exceptionResult;

    }

}

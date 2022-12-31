package com.hk.im.common.error;


import com.hk.im.common.resp.ResultCode;

/**
 * @author : HK意境
 * @ClassName : BusinessException
 * @date : 2022/10/26 23:41
 * @description : 请求链路出现异常: 请求异常，web框架处理异常
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class ApiException extends BaseException {

    public ApiException(){
        this.type = ExceptionType.ApiException;
    }

    public ApiException(ResultCode resultCode) {
        this.code = resultCode.code();
        this.message = resultCode.message();
        this.type = ExceptionType.ApiException;
        this.causes = resultCode.message();
    }

    public ApiException(String message, Integer code) {
        this.message = message;
        this.causes = message;
        this.type = ExceptionType.ApiException;
    }

    public ApiException(String message, Integer code, Exception exception) {
        this.message = message;
        this.causes = message;
        this.exceptionObject = exception;
        this.type = ExceptionType.ApiException;
    }
}

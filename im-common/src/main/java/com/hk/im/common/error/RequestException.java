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
public class RequestException extends BaseException {

    public RequestException(ResultCode resultCode) {
        this.code = resultCode.code();
        this.message = resultCode.message();
        this.type = ExceptionType.RequestException;
        this.causes = resultCode.message();
    }

    public RequestException(String message, Integer code) {
        this.message = message;
        this.causes = message;
        this.type = ExceptionType.RequestException;
    }

    public RequestException(String message, Integer code, Exception exception) {
        this.message = message;
        this.causes = message;
        this.exceptionObject = exception;
        this.type = ExceptionType.RequestException;
    }
}

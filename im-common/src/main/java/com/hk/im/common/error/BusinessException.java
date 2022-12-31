package com.hk.im.common.error;


import com.hk.im.common.resp.ResultCode;

/**
 * @author : HK意境
 * @ClassName : BusinessException
 * @date : 2022/10/26 23:41
 * @description : 业务逻辑处理异常
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class BusinessException extends BaseException {

    public BusinessException(ResultCode resultCode) {
        this.code = resultCode.code();
        this.message = resultCode.message();
        this.type = ExceptionType.BusinessException;
        this.causes = resultCode.message();
    }

    public BusinessException(String message, Integer code, Exception exception) {
        this.message = message;
        this.causes = message;
        this.exceptionObject = exception;
        this.type = ExceptionType.BusinessException;
    }
}

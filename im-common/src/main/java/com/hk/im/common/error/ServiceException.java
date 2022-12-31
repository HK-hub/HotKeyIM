package com.hk.im.common.error;


import com.hk.im.common.resp.ResultCode;

/**
 * @author : HK意境
 * @ClassName : BusinessException
 * @date : 2022/10/26 23:41
 * @description : 依赖的第三方服务，组件等出现异常
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class ServiceException extends BaseException {

    public ServiceException(ResultCode resultCode) {
        this(resultCode.message(), resultCode.code());
    }

    public ServiceException(ResultCode resultCode, Exception e) {
        this(resultCode.message(), resultCode.code(), e);
    }


    public ServiceException(String message, Integer code) {
        this.message = message;
        this.causes = message;
        this.code = code;
        this.type = ExceptionType.CustomException;
    }

    public ServiceException(String message, Integer code, Exception exception) {
        this.message = message;
        this.causes = message;
        this.code = code;
        this.exceptionObject = exception;
        this.type = ExceptionType.CustomException;
    }
}

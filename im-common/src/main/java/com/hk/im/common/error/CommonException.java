package com.hk.im.common.error;


import com.hk.im.common.resp.ResultCode;

/**
 * @author : HK意境
 * @ClassName : BusinessException
 * @date : 2022/10/26 23:41
 * @description : 普通异常，一些公共依赖，工具异常
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class CommonException extends BaseException {

    public CommonException(ResultCode resultCode) {
        this(resultCode.message(), resultCode.code());
    }

    public CommonException(ResultCode resultCode, Exception e) {
        this(resultCode.message(), resultCode.code(), e);
    }


    public CommonException(String message, Integer code) {
        this.message = message;
        this.causes = message;
        this.type = ExceptionType.RuntimeException;
    }

    public CommonException(String message, Integer code, Exception exception) {
        this.message = message;
        this.causes = message;
        this.exceptionObject = exception;
        this.type = ExceptionType.CustomException;
    }
}

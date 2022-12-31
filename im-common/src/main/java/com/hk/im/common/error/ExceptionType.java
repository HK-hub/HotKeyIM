package com.hk.im.common.error;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public enum ExceptionType {

    // 异常
    Exception("exception"),
    // 运行时异常
    RuntimeException("runtimeException"),
    // API 调用异常
    ApiException("api exception"),
    // 自定义异常
    CustomException("customException"),
    // 业务异常
    BusinessException("businessException"),
    // 链路异常(请求环节出现异常:参数，路径，方法等)
    RequestException("requestException"),
    // 服务异常
    ServiceException("serviceException");

    // 异常类型
    protected String type;

    ExceptionType(String type) {
        this.type = type;
    }
}
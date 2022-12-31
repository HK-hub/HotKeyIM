package com.hk.im.common.error;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author : HK意境
 * @ClassName : BaseException
 * @date : 2022/10/26 21:47
 * @description : 异常基类
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class BaseException extends Exception {

    // 链路追踪 id
    private String traceId;
    // 错误/异常 消息
    protected String message;
    // 错误/异常 原因
    protected String causes;
    // 错误/异常 代码
    protected Integer code;
    // 错误/异常 类型
    protected ExceptionType type;
    // 错误/异常 对象
    protected Exception exceptionObject;
    // 错误时间
    protected LocalDateTime dateTime = LocalDateTime.now();


}

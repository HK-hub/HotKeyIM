package com.hk.im.admin.interceptor;

import cn.hutool.core.lang.UUID;
import com.hk.im.common.consntant.ReqRespConstants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : LogInterceptor
 * @date : 2023/2/19 23:59
 * @description : 日志，链路追踪 拦截器
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class LogInterceptor implements AsyncHandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 获取 tranceId
        String traceId = request.getHeader(ReqRespConstants.TRACE_ID);
        if (Objects.isNull(traceId)) {
            // 没有 traceId, 创建一个
            traceId = UUID.fastUUID().toString();
        }

        // 放入 MDC
        MDC.put(ReqRespConstants.TRACE_ID, traceId);
        // 设置响应头
        response.addHeader(ReqRespConstants.TRACE_ID, traceId);

        // 打印日志
        String ip = request.getRemoteAddr();
        log.info("[traceId={}],[ip={}],[uri={}],[method={}],[time={}]",
                traceId, ip, request.getRequestURI(), request.getMethod(), LocalDateTime.now());

        return true;
    }

    /**
     * handler 开始处理
     *
     * @param request
     * @param response
     * @param handler
     *
     * @throws Exception
     */
    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        AsyncHandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //调用结束后删除
        MDC.remove(ReqRespConstants.TRACE_ID);
    }
}

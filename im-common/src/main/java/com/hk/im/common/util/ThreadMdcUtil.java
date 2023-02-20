package com.hk.im.common.util;

import com.hk.im.common.consntant.ReqRespConstants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.Callable;

@Slf4j
public class ThreadMdcUtil {
    public static void setTraceIdIfAbsent() {
        if (MDC.get(ReqRespConstants.TRACE_ID) == null) {
            MDC.put(ReqRespConstants.TRACE_ID, TraceIdUtil.getTraceId());
        }
    }

    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }

    public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
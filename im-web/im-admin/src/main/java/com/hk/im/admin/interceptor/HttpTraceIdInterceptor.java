package com.hk.im.admin.interceptor;

import com.hk.im.common.consntant.ReqRespConstants;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : HttpTraceIdInterceptor
 * @date : 2023/2/20 10:45
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class HttpTraceIdInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        String traceId = MDC.get(ReqRespConstants.TRACE_ID);
        if (Objects.nonNull(traceId)) {
            request.getHeaders().add(ReqRespConstants.TRACE_ID, traceId);
        }

        return execution.execute(request,body);
    }
}

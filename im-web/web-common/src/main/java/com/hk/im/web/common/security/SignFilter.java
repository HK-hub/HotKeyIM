package com.hk.im.web.common.security;

import com.alibaba.fastjson2.JSON;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

/**
 * @author : HK意境
 * @ClassName : SignFilter
 * @date : 2023/2/23 12:30
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@WebFilter(urlPatterns = "")
public class SignFilter implements Filter {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    //从fitler配置中获取sign过期时间
    private Long signMaxTime;

    private static final String NONCE_KEY = "security:nonce:x-nonce-";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        // 类型转换
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        // 日志
        log.info("过滤URL：{}", httpRequest.getRequestURL());

        HttpServletRequestWrapper requestWrapper = new SignRequestWrapper(httpRequest);
        // 构造 HttpRequestHeader
        RequestHeader requestHeader = RequestHeader.builder()
                // ip地址加设备mac地址加时间戳
                .nonce(httpRequest.getHeader("x-Nonce"))
                .timestamp(Long.parseLong(httpRequest.getHeader("X-Time")))
                .sign(httpRequest.getHeader("X-Sign"))
                .build();

        //验证请求头是否存在
        if(StringUtils.isEmpty(requestHeader.getSign()) || ObjectUtils.isEmpty(requestHeader.getTimestamp()) || StringUtils.isEmpty(requestHeader.getNonce())){
            responseFail(httpResponse, ResultCode.ILLEGAL_HEADER);
            return;
        }

        /*
         * 1.重放验证
         * 判断timestamp时间戳与当前时间是否超过60s（过期时间根据业务情况设置）,如果超过了就提示签名过期。
         */
        long now = System.currentTimeMillis();

        if ((now - requestHeader.getTimestamp()) / 1000 > signMaxTime) {
            responseFail(httpResponse,ResultCode.BAD_REQUEST);
            return;
        }

        //2. 判断nonce
        Boolean nonceExists = this.stringRedisTemplate.hasKey(NONCE_KEY + requestHeader.getNonce());
        if(BooleanUtils.isTrue(nonceExists)){
            //请求重复
            responseFail(httpResponse,ResultCode.BAD_REQUEST);
            return;
        }else {
            // 60s 过期
            this.stringRedisTemplate.opsForValue().set(NONCE_KEY+requestHeader.getNonce(), requestHeader.getNonce(),
                    signMaxTime, TimeUnit.SECONDS);
        }

        boolean accept;
        SortedMap<String, String> paramMap;
        switch (httpRequest.getMethod()) {
            case "GET": {
                paramMap = HttpDataUtil.getUrlParams(requestWrapper);
                accept = SignUtil.verifySign(paramMap, requestHeader);
                break;
            }
            case "POST": {
                paramMap = HttpDataUtil.getBodyParams(requestWrapper);
                accept = SignUtil.verifySign(paramMap, requestHeader);
                break;
            }
            default:
                accept = true;
                break;
        }
        if (accept) {
            filterChain.doFilter(requestWrapper, servletResponse);
        } else {
            responseFail(httpResponse,ResultCode.BAD_REQUEST);
            return;
        }

        // 放行
        filterChain.doFilter(requestWrapper, servletResponse);
    }


    /**
     * 响应失败
     * @param response
     * @param resultCode
     */
    private void responseFail(HttpServletResponse response, ResultCode resultCode) throws IOException {

        ResponseResult result = new ResponseResult(resultCode);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSON.toJSONString(result));
        response.getWriter().flush();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String signTime = filterConfig.getInitParameter("signMaxTime");
        signMaxTime = Long.parseLong(signTime);
    }

}

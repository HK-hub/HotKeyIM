package com.hk.im.web.common.security;

import com.alibaba.fastjson2.JSON;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Stream;

/**
 * @author : HK意境
 * @ClassName : HttpDataUtil
 * @date : 2023/2/23 11:31
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@UtilityClass
public class HttpDataUtil {

    /**
     * 获取 post 请求 body 参数
     * @param request
     * @return
     */
    public static SortedMap<String, String> getBodyParams(final HttpServletRequest request) throws IOException {

        byte[] bodyBytes = StreamUtils.copyToByteArray(request.getInputStream());

        return JSON.parseObject(new String(bodyBytes), SortedMap.class);
    }



    /**
     * get请求处理：将URL请求参数转换成SortedMap
     */
    public static SortedMap<String, String> getUrlParams(HttpServletRequest request) {
        String param = "";
        SortedMap<String, String> result = new TreeMap<>();

        if (StringUtils.isEmpty(request.getQueryString())) {
            return result;
        }

        try {
            param = URLDecoder.decode(request.getQueryString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String[] params = param.split("&");
        for (String s : params) {
            String[] array=s.split("=");
            result.put(array[0], array[1]);
        }
        return result;
    }


}

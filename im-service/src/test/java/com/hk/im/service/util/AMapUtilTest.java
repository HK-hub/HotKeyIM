package com.hk.im.service.util;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.StringJoiner;

/**
 * @author : HK意境
 * @ClassName : AMapUtilTest
 * @date : 2023/4/14 8:43
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class AMapUtilTest {

    @Test
    public void test() {

        // 查询位置信息: 逆地址编码查询
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("key", "fb8eaa292782a00000e337d4635c6df8");
        parameters.put("location", "" + 98.458501 + "," + 25.012389);
        // parameters.put("sig", "0cf0942b1590c335f9d05faa568e14a6");

        String jsonData = HttpUtil.get("https://restapi.amap.com/v3/geocode/regeo", parameters);

        JSONObject jsonObject = JSON.parseObject(jsonData);
        log.info("request location mapping address: {}", jsonObject);

    }



}




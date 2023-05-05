package com.hk.im.flow.data.sensitive.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : HK意境
 * @ClassName : SensitiveWordUtilTest
 * @date : 2023/5/5 20:33
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
class SensitiveWordUtilTest {

    @Test
    public void testSensitive() {
        // 初始化 敏感词 列表
        List<String> list = new ArrayList<>();
        list.add("冰毒");
        list.add("特朗普");
        SensitiveWordUtil.initMap(list);
        // 待查询文本
        String content="我是一个好人，买卖冰毒是违法的特朗普";
        // 匹配文本
        Map<String, Integer> map = SensitiveWordUtil.matchWords(content);
        System.out.println(map);
    }

}
package com.hk.im.flow.data.sensitive.service;

import cn.hutool.core.lang.Assert;
import com.github.houbb.sensitive.word.api.ISensitiveWordReplace;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.hk.im.flow.data.sensitive.strategy.MySensitiveWordReplaceStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : HK意境
 * @ClassName : SensitiveWordServiceTest
 * @date : 2023/5/5 22:30
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
class SensitiveWordServiceTest {

    /**
     * 自定替换策略
     * @since 0.2.0
     */
    @Test
    public void defineReplaceTest() {
        final String text = "HK在github提交了代码有关黄海，意境的issue";

        ISensitiveWordReplace replace = new MySensitiveWordReplaceStrategy();
        String result = SensitiveWordHelper.replace(text, replace);

        System.out.println(result);
    }


}
package com.hk.im.flow.data.sensitive.service;

import com.github.houbb.sensitive.word.api.ISensitiveWordReplace;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.github.houbb.sensitive.word.support.allow.WordAllows;
import com.github.houbb.sensitive.word.support.deny.WordDenys;
import com.hk.im.flow.data.sensitive.strategy.MySensitiveWordReplaceStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : SensitiveWordService
 * @date : 2023/5/5 21:01
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Service
public class SensitiveWordService {

    @Resource
    private SensitiveWordBs sensitiveWordBs;
    @Resource
    private ISensitiveWordReplace sensitiveWordReplace;

    /**
     * 对文本进行脱敏
     * @param content 带脱敏文本
     * @return 脱敏后的文本
     */
    public String sensitiveWords(String content) {

        boolean contains = this.sensitiveWordBs.contains(content);

        if (BooleanUtils.isFalse(contains)) {
            // 不包含铭感信息
            return content;
        }

        // 包含铭感信息进行自定义脱敏
        String sensitive = this.sensitiveWordBs.replace(content, sensitiveWordReplace);

        // 响应脱敏后的文本
        return sensitive;
    }


}

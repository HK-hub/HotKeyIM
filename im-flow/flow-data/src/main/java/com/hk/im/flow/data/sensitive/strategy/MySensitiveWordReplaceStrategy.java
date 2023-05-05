package com.hk.im.flow.data.sensitive.strategy;

import com.github.houbb.heaven.util.lang.CharUtil;
import com.github.houbb.sensitive.word.api.ISensitiveWordReplace;
import com.github.houbb.sensitive.word.api.ISensitiveWordReplaceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author : HK意境
 * @ClassName : MySensitiveWordReplaceStrategy
 * @date : 2023/5/5 21:50
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class MySensitiveWordReplaceStrategy implements ISensitiveWordReplace {

    @Override
    public String replace(ISensitiveWordReplaceContext context) {

        String sensitiveWord = context.sensitiveWord();
        // 自定义不同的敏感词替换策略，可以从数据库等地方读取

        // 对我不利的铭感词
        if("HK".equals(sensitiveWord)) {
            return "WT";
        } else if("意境".equals(sensitiveWord)) {
            return "郑辉";
        } else if ("黄海".equals(sensitiveWord)) {
            return "李列伟";
        } else if ("hk-hub".equalsIgnoreCase(sensitiveWord)) {
            return "justing3go";
        }
        // 为了群氛围的健康发展的敏感词
        else if ("太强了".equals(sensitiveWord)) {
            return "大菜逼";
        } else if ("我是小丑".equals(sensitiveWord)) {
            return "我也一样";
        } else if ("羡慕".equals(sensitiveWord)) {
            return "一般般啦";
        } else if ("卷".equals(sensitiveWord)) {
            return "闲";
        }


        // 其他默认使用 * 代替
        int wordLength = context.wordLength();
        return CharUtil.repeat('*', wordLength);
    }
}

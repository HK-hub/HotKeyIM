package com.hk.im.flow.data.sensitive.config;

import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.support.allow.WordAllows;
import com.github.houbb.sensitive.word.support.deny.WordDenys;
import com.hk.im.flow.data.sensitive.word.MyWordAllow;
import com.hk.im.flow.data.sensitive.word.MyWordDeny;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : SensitiveWordConfig
 * @date : 2023/5/5 22:51
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Configuration
public class SensitiveWordConfig {

    @Resource
    private MyWordAllow myWordAllow;

    @Resource
    private MyWordDeny myWordDeny;

    /**
     * 初始化引导类
     * @return 初始化引导类
     * @since 1.0.0
     */
    @Bean
    public SensitiveWordBs sensitiveWordBs() {
        SensitiveWordBs sensitiveWordBs = SensitiveWordBs.newInstance()
                .wordAllow(WordAllows.chains(WordAllows.system(), myWordAllow))
                .wordDeny(WordDenys.chains(WordDenys.system(), myWordDeny))
                // 各种其他配置
                .init();

        return sensitiveWordBs;
    }


}

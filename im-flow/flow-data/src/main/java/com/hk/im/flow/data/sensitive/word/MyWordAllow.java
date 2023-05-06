package com.hk.im.flow.data.sensitive.word;

import com.github.houbb.sensitive.word.api.IWordAllow;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author : HK意境
 * @ClassName : MyWordAllow
 * @date : 2023/5/5 22:53
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Component
public class MyWordAllow implements IWordAllow {

    private List<String> words = new ArrayList<>();

    @Override
    public List<String> allow() {
        return words;
    }


    /**
     * 加载白名单
     */
    @PostConstruct
    public void init() {
        words = Arrays.asList("码", "代码", "消息", "QQ", "微信", "qq");
    }


}

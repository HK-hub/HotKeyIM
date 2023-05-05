package com.hk.im.flow.data.sensitive.word;

import com.github.houbb.sensitive.word.api.IWordDeny;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName : MyWordDeny
 * @author : HK意境
 * @date : 2023/5/5 22:41
 * @description : 自定义敏感词
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Component
public class MyWordDeny implements IWordDeny {

    private List<String> words = new ArrayList<>();

    @Override
    public List<String> deny() {
        return this.words;
    }

    /**
     * 加载铭感词脚本
     */
    @PostConstruct
    public void init() {
        words = Arrays.asList("HK", "意境", "hk-hub", "黄海");
    }


}
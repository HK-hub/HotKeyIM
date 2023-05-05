package com.hk.im.flow.data.sensitive.word;

import com.github.houbb.sensitive.word.api.IWordAllow;
import org.springframework.stereotype.Component;

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
    @Override
    public List<String> allow() {
        return List.of("代码");
    }
}

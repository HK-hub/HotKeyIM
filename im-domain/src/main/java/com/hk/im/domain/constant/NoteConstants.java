package com.hk.im.domain.constant;

import lombok.Data;

/**
 * @author : HK意境
 * @ClassName : NoteConstants
 * @date : 2023/5/22 13:35
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
public class NoteConstants {


    /**
     * 笔记说说查看策略
     */
    public static enum ViewStrategy {

        // 所有人可见
        ALL,
        // 仅好友可见
        FRIEND,
        // 仅个人可见
        SELF,
        // 过滤：指定某些人可见，指定某些人不可见
        FILTER,


    }

}

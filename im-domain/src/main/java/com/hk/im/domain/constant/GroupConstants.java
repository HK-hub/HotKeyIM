package com.hk.im.domain.constant;

import lombok.Getter;

/**
 * @author : HK意境
 * @ClassName : GroupConstants
 * @date : 2023/1/2 16:19
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class GroupConstants {



    // 群聊分类
    @Getter
    public enum GroupCategory {
        ;
        private String category;

        GroupCategory(String category) {
            this.category = category;
        }
    }



}

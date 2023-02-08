package com.hk.im.domain.vo;

import lombok.Data;

/**
 * @author : HK意境
 * @ClassName : FriendGroupVO
 * @date : 2023/2/8 22:06
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
public class FriendGroupVO {

    /**
     * 分组id
     */
    private Long id;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 分组好友数量
     */
    private Integer count;

}

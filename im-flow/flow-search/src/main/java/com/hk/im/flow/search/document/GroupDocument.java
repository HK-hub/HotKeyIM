package com.hk.im.flow.search.document;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : GroupDocument
 * @date : 2023/5/18 11:40
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class GroupDocument {

    /**
     * 群id
     */
    private Long id;

    /**
     * 群账号
     */
    private Long groupAccount;

    /**
     * 群聊名称
     */
    private String groupName;

    /**
     * 群描述
     */
    private String description;

}
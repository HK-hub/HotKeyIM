package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : ModifyGroupInfoRequest
 * @date : 2023/1/22 20:39
 * @description : 修改群信息
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class ModifyGroupInfoRequest {

    private String groupId;

    private String groupName;

    // 群描述
    private String description;

    private String location;

    // 群分类
    private String category;

    // 群标签
    private String tags;


}

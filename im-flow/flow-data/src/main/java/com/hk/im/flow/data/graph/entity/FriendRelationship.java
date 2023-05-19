package com.hk.im.flow.data.graph.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : FriendRelationship
 * @date : 2023/5/17 17:55
 * @description : 定义用户之间的好友关系属性
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class FriendRelationship {

    private Long id;

    private Long friendId;

    // 相当于 @StartNode
    private Person parent;

    private String relation;

    /**
     * 状态：0.陌生人(临时会话)，1.好友，2.黑名单，3.特别关心，4.删除
     */
    private Integer status;

    /**
     * 分组:如果不是好友，默认临时会话
     */

    private String group;

    /**
     * 分组id
     */

    private Long groupId;

    /**
     * 备注姓名
     */

    private String nickname;

    /**
     * 备注姓名
     */

    private String remarkName;

    /**
     * 备注信息
     */

    private String remarkInfo;

    /**
     * 是否机器人：0.否，1.是
     */

    private Boolean robot;

    /**
     * 是否置顶: 0.否，1.是
     */

    private Boolean top;

}

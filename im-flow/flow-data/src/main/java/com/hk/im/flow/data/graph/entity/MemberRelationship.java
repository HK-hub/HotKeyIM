package com.hk.im.flow.data.graph.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : MemberRelationship
 * @date : 2023/5/18 14:09
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class MemberRelationship {

    private Long id;

    /**
     * 群id
     */
    private Long groupId;

    /**
     * 群号
     */
    private Long groupAccount;

    // 相当于 @StartNode
    private GroupNode parent;

    /**
     * 群成员id
     */
    private Long memberId;

    /**
     * 群成员群外昵称
     */
    private String memberUsername;

    /**
     * 群成员的群内昵称
     */
    private String memberRemarkName;

    /**
     * 群员角色:1.普通成员，2.管理员，3.群主
     */
    private Integer memberRole;

    /**
     * 群分组，群分类
     */
    private String groupCategory;


    /**
     * 群状态：1.加群，2.退群，3.群黑名单(前提是已经被踢出群聊)
     */
    private Integer status;


}

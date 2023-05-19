package com.hk.im.flow.data.graph.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author : HK意境
 * @ClassName : GroupNode
 * @date : 2023/5/18 13:53
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class GroupNode {

    /**
     * 群id
     */

    private Long id;


    private Long groupAccount;

    /**
     * 群聊名称
     */
    private String groupName;

    /**
     * 群描述
     */
    private String description;

    /**
     * 群类型:0.未知，1.兴趣爱好，2.行业交流，3.生活休闲，3.学习考试，4.娱乐游戏，5.置业安家，6.品牌产品，7.粉丝，8.同学同事，9.家校师生
     */
    private String groupType;

    private Set<MemberRelationship> members = new HashSet<>();

}

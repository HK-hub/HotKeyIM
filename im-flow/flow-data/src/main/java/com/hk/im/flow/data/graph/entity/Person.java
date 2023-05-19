package com.hk.im.flow.data.graph.entity;


import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : HK意境
 * @ClassName : Person
 * @date : 2023/5/17 17:13
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class Person {

    private Long id;

    /**
     * 用户名，昵称
     */
    private String username;

    /**
     * 账号，类比QQ号,唯一性
     */
    private String account;

    // 注意这些关系最终的箭头指向是当前实体，即TargetNode（Friend）->当前定义Relationship的实体（Person）
    private List<FriendRelationship> friends = new ArrayList<>();


    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}

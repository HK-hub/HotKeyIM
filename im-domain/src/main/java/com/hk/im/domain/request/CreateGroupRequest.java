package com.hk.im.domain.request;

import lombok.Data;

import java.util.List;

/**
 * @author : HK意境
 * @ClassName : CreateGroupRequest
 * @date : 2023/1/2 16:13
 * @description : 创建群聊请求
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
public class CreateGroupRequest {

    // 群主id: 用户id
    private Long masterId;

    // 群聊名称
    private String groupName;

    // 群聊初始人员: 传入 account 或 userId 均可
    private List<Long> initialGroupMembers;

    // 按照分类创建
    private String category;

    // 群聊头像
    private String avatar;


    // 描述信息
    private String description;

    // 群地点
    private String location;

    // 群规模:最大人数限制
    private Integer capacity;

    // 是否公开
    private Boolean enablePublic;

    // 加群验证方式
    private Integer verify;

    // 扩展信息
    private String extra;

}

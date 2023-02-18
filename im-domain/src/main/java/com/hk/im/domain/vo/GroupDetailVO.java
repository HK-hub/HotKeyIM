package com.hk.im.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : HK意境
 * @ClassName : GroupDetailVO
 * @date : 2023/2/18 16:03
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class GroupDetailVO {

    // 群id
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
     * 群聊头像
     */
    private String groupAvatar;

    /**
     * 群描述
     */
    private String description;

    /**
     * 群类型:0.未知，1.兴趣爱好，2.行业交流，3.生活休闲，3.学习考试，4.娱乐游戏，5.置业安家，6.品牌产品，7.粉丝，8.同学同事，9.家校师生
     */
    private Integer groupType;

    /**
     * 群二维码
     */
    private String qrcode;

    /**
     * 群人数
     */
    private Integer memberCount;

    /**
     * 群主
     */
    private Long groupMaster;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    // 群名片,个人群昵称
    private String visitCard;

    // 最新群公告
    private String notice;

}




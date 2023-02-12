package com.hk.im.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author : HK意境
 * @ClassName : GroupMemberVO
 * @date : 2023/2/12 19:53
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class GroupMemberVO {

    /**
     * 群成员id
     */
    private String memberId;

    /**
     * 群成员群外昵称
     */
    private String memberUsername;

    /**
     * 群成员的群内昵称
     */
    private String memberRemarkName;

    /**
     * 群成员头像(缩略图)
     */
    private String memberAvatar;

    /**
     * 群员角色:1.普通成员，2.管理员，3.群主
     */
    private Integer memberRole;

    /**
     * 群分组，群分类
     */
    private String groupCategory;

    /**
     * 禁言时间：表示禁止发言的结束时间
     */
    private LocalDateTime gagTime;

    private String lastAckMessage;

    /**
     * 群状态：1.加群，2.退群，3.群黑名单(前提是已经被踢出群聊)
     */
    private Integer status;

    /**
     * 创建时间(加群时间)
     */
    private LocalDateTime createTime;



}

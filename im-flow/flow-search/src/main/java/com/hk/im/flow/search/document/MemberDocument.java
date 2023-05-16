package com.hk.im.flow.search.document;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author : HK意境
 * @ClassName : MemberDocument
 * @date : 2023/5/16 15:48
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class MemberDocument {

    /**
     * id
     */
    private Long id;

    /**
     * 群id
     */
    private Long groupId;

    /**
     * 群号
     */
    private Long groupAccount;

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
     * 群分组，群分类
     */
    private String groupCategory;

    /**
     * 群状态：1.加群，2.退群，3.群黑名单(前提是已经被踢出群聊)
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}

package com.hk.im.flow.search.document;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author : HK意境
 * @ClassName : FriendDocument
 * @date : 2023/5/16 15:24
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class FriendDocument {

    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 好友id
     */
    private Long friendId;

    /**
     * 状态：0.陌生人(临时会话)，1.好友，2.黑名单，3.特别关心，4.删除
     */
    private Integer relation;

    /**
     * 好友昵称
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
     * 创建时间
     */
    private LocalDateTime createTime;

}

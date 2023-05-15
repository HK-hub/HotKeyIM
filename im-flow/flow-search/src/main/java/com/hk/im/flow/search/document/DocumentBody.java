package com.hk.im.flow.search.document;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author : HK意境
 * @ClassName : DocumentBody
 * @date : 2023/5/15 19:53
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class DocumentBody {

    // 对应实体id
    private Long id;

    // 用于搜索好友的时候适配备注，原名等
    private String username;

    // 内容
    private String content;

    // 数据实体类型: 用户，好友，群聊，群成员，聊天记录等
    private Integer type;

    // 创建时间
    private LocalDateTime createTime;

    private LocalDateTime updateTime;


    public static enum Type {

        // 其他
        DEFAULT,
        // 消息
        MESSAGE,
        // 好友
        FRIEND,
        // 群聊
        GROUP,
        // 群成员
        MEMBER,
        ;

    }


}

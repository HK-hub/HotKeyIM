package com.hk.im.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author : HK意境
 * @ClassName : GroupAnnouncementVO
 * @date : 2023/2/12 20:05
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class GroupAnnouncementVO {

    /**
     * 公告id
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
     * 群公告类型：1.普通公告，2.置顶公告, 3.新人公告
     */
    private Integer type;

    /**
     * 公告撰写者
     */
    private Long author;

    /**
     * 作者名称
     */
    private String authorName;

    /**
     * 作者头像
     */
    private String avatar;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 是否置顶
     */
    private Boolean top;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

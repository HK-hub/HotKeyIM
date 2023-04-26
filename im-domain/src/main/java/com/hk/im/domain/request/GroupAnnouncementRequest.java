package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : GroupAnnouncementRequest
 * @date : 2023/1/23 18:22
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class GroupAnnouncementRequest {

    /**
     * 群id
     */
    private Long groupId;

    /**
     * 公告id
     */
    private Long announcementId;


    /**
     * 标题
     */
    private String title;


    /**
     * 内容
     */
    private String content;

    /**
     * 是否置顶
     */
    private Boolean top = false;


    /**
     * 确认公告
     */
    private Boolean confirm;

}

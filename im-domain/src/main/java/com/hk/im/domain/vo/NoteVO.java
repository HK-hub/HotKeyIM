package com.hk.im.domain.vo;

import com.hk.im.domain.entity.Category;
import com.hk.im.domain.entity.CloudResource;
import com.hk.im.domain.entity.Tag;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : HK意境
 * @ClassName : NoteVO
 * @date : 2023/3/27 17:14
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class NoteVO {


    private Long id;

    /**
     * 文章标题
     */
    private String title;


    // 封面
    private String cover;

    // 摘要
    private String summary;

    /**
     * markdown 格式的笔记内容
     */
    private String mdContent;

    /**
     * HTML 解析格式的笔记内容
     */
    private String content;

    /**
     * 笔记状态：0.草稿，1.发布，2.撤回，3.删除
     */
    private Integer status;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    private List<Tag> tags;

    private Category category;

}

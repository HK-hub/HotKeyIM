package com.hk.im.domain.bo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author : HK意境
 * @ClassName : EmoticonBO
 * @date : 2023/2/23 16:33
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class EmoticonBO {

    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 表情分类, 1.文本，2.表情
     */
    private Integer type;

    /**
     * 表情内容
     */
    private String content;

    /**
     * 图片链接
     */
    private String url;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;



}

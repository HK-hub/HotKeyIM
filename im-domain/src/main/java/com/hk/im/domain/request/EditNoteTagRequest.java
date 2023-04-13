package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : EditNoteTagRequest
 * @date : 2023/3/28 9:44
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class EditNoteTagRequest {

    private Long tagId;

    private String name;

    private String avatar;

    private String description;

}

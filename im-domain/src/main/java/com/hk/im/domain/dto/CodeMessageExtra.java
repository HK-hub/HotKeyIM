package com.hk.im.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : CodeMessageExtra
 * @date : 2023/3/16 16:17
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class CodeMessageExtra extends BaseMessageExtra{

    // 代码块名称
    private String name;

    private String code;

    // 代码语言类型
    private String lang;

    // 代码长度
    private Integer length;

    // 代码行数
    private Integer lines;


}

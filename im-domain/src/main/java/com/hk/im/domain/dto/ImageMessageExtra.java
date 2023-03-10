package com.hk.im.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : HK意境
 * @ClassName : ImageMessageExtra
 * @date : 2023/3/9 22:46
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Data
@Accessors(chain = true)
public class ImageMessageExtra {

    // 文件名称
    private String fileName;

    // 文件原始名称
    private String originalFileName;

    // 文件大小: KB
    private Double size;

    // 文件扩展名
    private String extension;

    // 文件子类型
    private Integer fileSubType;

    // 文件上传用户
    private String username;

    // 上传用户
    private Long uploader;

    // 接收者
    private Long receiver;


    public static enum FileSubType {

        DEFAULT,
        IMAGE,
        VOICE,
        VIDEO,
        FILE,

    }



}

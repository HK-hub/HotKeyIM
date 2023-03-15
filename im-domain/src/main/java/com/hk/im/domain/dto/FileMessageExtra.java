package com.hk.im.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : HK意境
 * @ClassName : FileMessageExtra
 * @date : 2023/3/10 15:19
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Data
@Accessors(chain = true)
public class FileMessageExtra extends BaseMessageExtra{

    // 文件名称
    protected String fileName;

    // 文件原始名称
    protected String originalFileName;

    // 文件大小: 字节
    protected Long size;

    // 文件扩展名
    protected String extension;

    // 文件子类型
    protected Integer fileSubType;

    // 文件上传用户
    protected String username;

    // 上传用户
    protected Long uploader;

    // 接收者
    protected Long receiver;

    // url 链接
    protected String url;

    // 本地文件位置
    protected String localPath;

    protected String md5;


}

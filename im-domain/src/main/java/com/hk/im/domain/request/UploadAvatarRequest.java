package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : HK意境
 * @ClassName : UploadAvatarRequest
 * @date : 2023/2/19 13:52
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class UploadAvatarRequest {

    /**
     * 1.用户头像，2.群聊头像
     */
    private Integer type;

    /**
     * 需要上传头像的目标id
     */
    private String targetId;


    /**
     * 操作者
     */
    private String operatorId;


    /**
     * 头像文件
     */
    private MultipartFile file;

    public static enum AvatarType {
        DEFAULT,
        USER,
        GROUP
    }



}

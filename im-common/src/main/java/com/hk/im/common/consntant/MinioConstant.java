package com.hk.im.common.consntant;

import lombok.Getter;

/**
 * @author : HK意境
 * @ClassName : MinioConstant
 * @date : 2023/1/1 17:17
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class MinioConstant {

    // 用户大头像: public_big_avatar_
    public static final String USER_BIG_AVATAR_PREFIX = AccessPolicyEnum.PUBLIC.policy + ResourceNamePrefixEnum.BIG_AVATAR.prefix;
    // 用户大头像: public_mini_avatar_
    public static final String USER_MINI_AVATAR_PREFIX = AccessPolicyEnum.PUBLIC.policy + ResourceNamePrefixEnum.MINI_AVATAR.prefix;
    // 用户二维码地址
    public static final String USER_QRCODE_PREFIX = AccessPolicyEnum.PUBLIC.policy + ResourceNamePrefixEnum.QRCODE.prefix;
    // 好友私聊图片地址
    public static final String PRIVATE_CHAT_IMAGE = AccessPolicyEnum.PUBLIC.policy + ResourceNamePrefixEnum.IMAGE.prefix;
    // 用户头像空间
    public static final String USER_AVATAR_PATH = "/user/avatar/";
    // 用户二维码
    public static final String USER_QR_CODE_PATH = "/user/qrcode/";
    // 群聊文件，二维码
    public static final String GROUP_PATH = "/group/";
    // 群聊头像
    public static final String GROUP_AVATAR_PATH = "/avatar/";
    // 群二维码
    public static final String GROUP_QRCODE_PATH = "/qrcode/";
    // 群聊文件
    public static final String GROUP_FILE_PATH = "/group/file/";
    // 私聊图片
    public static final String PRIVATE_IMAGE_PATH = "/image/";
    // 临时文件目录
    public static final String TEM_PATH = "temp/upload/";


    @Getter
    public enum BucketEnum {

        System("system"),
        User("user"),
        Group("group"),
        Image("image"),
        File("file"),
        Note("note"),
        ;
        private String bucket;

        BucketEnum(String bucket) {
            this.bucket = bucket;
        }
    }


    /**
     * minio bucket 访问策略
     */
    @Getter
    public enum AccessPolicyEnum {

        PUBLIC("public_"),
        PRIVATE("private_"),
        CUSTOM("custom_");

        private String policy;

        AccessPolicyEnum(String policy) {
            this.policy = policy;
        }
    }


    /**
     * 文件Object 路径前缀: 文件夹
     */
    @Getter
    public enum DirectPathPrefixEnum {

        // 用户头像
        USER_AVATAR("user/avatar/"),
        // 用户二维码
        USER_QR("user/qr/"),
        ;

        private String path;

        DirectPathPrefixEnum(String path) {
            this.path = path;
        }
    }


    /**
     * 对象资源名称前缀
     */
    @Getter
    public enum ResourceNamePrefixEnum {

        // 头像
        GROUP_AVATAR("group_avatar_"),
        // 迷你头像
        MINI_AVATAR("avatar_mini_"),
        BIG_AVATAR("avatar_big_"),
        // 二维码
        QRCODE("qrcode_"),
        // 文件
        FILE("file_"),
        // 图片
        IMAGE("image_");

        private String prefix;

        ResourceNamePrefixEnum(String prefix) {
            this.prefix = prefix;
        }
    }

    /**
     * 获取群聊二维码路径
     * @param groupAccount
     * @return
     */
    public static String getGroupQrcodePath(Long groupAccount) {
        return GROUP_QRCODE_PATH + AccessPolicyEnum.PUBLIC.policy + ResourceNamePrefixEnum.QRCODE.prefix + groupAccount + ".jpg";
    }


    /**
     * 获取群聊头像路径
     * @param groupAccount
     * @return
     */
    public static String getGroupAvatarPath(String groupAccount) {
        return GROUP_AVATAR_PATH + AccessPolicyEnum.PUBLIC.policy + ResourceNamePrefixEnum.GROUP_AVATAR.prefix
                + groupAccount + ".png";
    }


    /**
     * 获取用户头像路径
      * @param targetId
     * @return
     */
    public static String getUserAvatarPath(String targetId) {

        String miniAvatar = MinioConstant.USER_MINI_AVATAR_PREFIX + targetId;
        return MinioConstant.USER_AVATAR_PATH + miniAvatar + ".jpg";
    }

    /**
     * 获取用户大头像路径
     * @param targetId
     * @return
     */
    public static String getUserBigAvatarPath(String targetId) {
        String bigAvatar = MinioConstant.USER_BIG_AVATAR_PREFIX + targetId;
        return MinioConstant.USER_AVATAR_PATH + bigAvatar + ".jpg";
    }

    /**
     * 好友私聊获取图片
     * @return String objectPath 图片路径
     */
    public static String getPrivateImagePath(String objectName) {
        return PRIVATE_CHAT_IMAGE  + objectName;
    }


    /**
     * 获取文件上传所在的文件夹下
     * @return
     */
    public static String getUploadFilePrefix() {
        return "/upload/";
    }


}

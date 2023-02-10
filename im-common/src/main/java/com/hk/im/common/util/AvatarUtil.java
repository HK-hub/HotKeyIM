package com.hk.im.common.util;

import com.feilong.accessor.keygenerator.UUIDKeyGenerator;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author : HK意境
 * @ClassName : AvatarUtil
 * @date : 2023/2/10 16:42
 * @description : 用户头像Util
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class AvatarUtil {

    public static final String avatarUrl = "https://www.gravatar.com/avatar/";
    private static final int defaultBigSize = 200;

    /**
     * //.append("d=retro&") github 风格头像
     * // 可选 d=""
     * // 404：如果没有任何图像与电子邮件哈希无关，则不加载任何图像，而是返回HTTP 404（找不到文件）响应
     * //mp：（神秘人物）一个人的简单卡通风格的轮廓（不随电子邮件哈希值而变化）
     * //identicon：基于电子邮件哈希的几何图案
     * //monsterid：生成的具有不同颜色，面孔等的“怪物”
     * //wavatar：生成的具有不同特征和背景的面孔
     * //retro：生成的令人敬畏的8位街机风格像素化面孔
     * //robohash：具有不同颜色，面部等的生成的机器人
     */
    public static final String[] styles = {"identicon", "monsterid", "wavatar", "retro", "robohash"};
    public static final ThreadLocalRandom random = ThreadLocalRandom.current();


    /**
     * 生成随机风格头像, 默认mini
     *
     * @return
     */
    public static String generateAvatar() {
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        StringBuilder avatar = new StringBuilder();
        avatar.append("https://www.gravatar.com/avatar/")
                .append(id + ".png")
                .append("?f=y&")
                .append("d=")
                .append(styles[random.nextInt(styles.length)])
                .append("&")
                .append("r=g");
        // 生成头像
        return avatar.toString();
    }


    /**
     * 生成大头像：200px
     *
     * @param size
     *
     * @return
     */
    public static String generateBigAvatar(int size) {
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        StringBuilder avatar = new StringBuilder();
        avatar.append("https://www.gravatar.com/avatar/")
                .append(id + ".png")
                .append("?f=y&")
                .append("d=")
                .append(styles[random.nextInt(styles.length)])
                .append("&")
                .append("r=g");
        // 设置大小
        avatar.append("&s=").append(size);
        return avatar.toString();
    }


    /**
     * 通过mini 头像获取对应的大头像
     * @param miniAvatar
     * @return
     */
    public static String getBigAvatar(String miniAvatar) {
        StringBuilder sb = new StringBuilder(miniAvatar);
        sb.append("&s=").append(defaultBigSize);
        return sb.toString();
    }


}

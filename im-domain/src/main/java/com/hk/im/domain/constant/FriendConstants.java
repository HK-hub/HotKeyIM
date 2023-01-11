package com.hk.im.domain.constant;

import lombok.Data;

/**
 * @author : HK意境
 * @ClassName : FriendConstants
 * @date : 2023/1/2 17:27
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
public class FriendConstants {


    // 好友关系
    public static enum FriendRelationship {

        // 陌生人:临时会话
        STRANGER,
        // 好友
        FRIEND,
        // 黑名单
        BLACK,
        // 特别关心
        CAREFUL,
        // 删除
        DELETED,
        // 置顶
        TOP
        ;

    }


    // 好友添加策略: 加好友策略：0.直接同意，1.验证，2.回答问题，3.输入密码
    public static enum FriendApplyPolicy {

        // 直接同意
        AGREE,
        // 需要好友审核
        VERIFY,
        // 回答问题
        PROBLEM,
        // 输入密码
        PASSWORD,
        ;
    }


    // 好友添加申请状态：状态:0.忽略，1.待处理，2.同意，3.拒绝
    public static enum FriendApplyStatus {
        // 忽略
        IGNORE,
        // 待处理
        WAITING,
        // 同意
        AGREE,
        // 拒绝
        REJECT,
        ;
    }


    // 好友申请类型:申请类型：1.好友申请，2.加群申请
    public static enum FriendApplyType {

        DEFAULT,
        FRIEND,
        GROUP,
        ;

    }


}

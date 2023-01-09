package com.hk.im.domain.request;

import lombok.Data;

/**
 * @author : HK意境
 * @ClassName : ModifyFriendInfoRequest
 * @date : 2023/1/9 18:53
 * @description : 修改用户信息，例如备注昵称，备注信息
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
public class ModifyFriendInfoRequest {


    private Integer action;

    // 好友id
    private String userId;

    // 需要被修改的好友id
    private String friendId;

    private String remarkName;

    private String remarkDescription;

    private String group;

    // 操作类型
    public static enum Action {

        remarkName,
        remarkDescription,
        group,
    }


}

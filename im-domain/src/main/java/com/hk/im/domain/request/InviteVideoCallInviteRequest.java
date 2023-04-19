package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : InviteVideoCallInviteRequest
 * @date : 2023/4/19 17:04
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class InviteVideoCallInviteRequest {

    private String cmd;
    // 聊天类型
    private Integer type = 1;
    // 接听者是谁
    private String listener;
    // 拨打者是谁
    private String dialer;

}

package com.hk.im.domain.request;

import lombok.Data;

/**
 * @author : HK意境
 * @ClassName : ApplyHandleRequest
 * @date : 2023/1/2 21:10
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
public class ApplyHandleRequest {

    //1.好友申请，2.加群申请
    private Integer type;

    // 申请单
    private String applyId;

    // 处理者id
    private String handlerId;

    // 好友申请消息处理:0.忽略，1.待处理，2.同意，3.拒绝
    private Integer operation;

    // 回复信息
    private String info;


}

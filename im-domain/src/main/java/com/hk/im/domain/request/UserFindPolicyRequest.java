package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : UserFindPolicyRequest
 * @date : 2023/2/16 22:20
 * @description : 用户发现模块策略
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class UserFindPolicyRequest {

    private String userId;

    // 当前请求第几页
    private Integer page = 1;

    // 每页多少条
    private Integer size = 19;

    private Integer offset = 0;

    private String name;


}

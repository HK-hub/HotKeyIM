package com.hk.im.domain.request;

import lombok.Data;

/**
 * @author : HK意境
 * @ClassName : FriendFindRequest
 * @date : 2023/1/2 13:50
 * @description : 好友发现，群发现请求
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
public class FriendFindRequest {

    private Integer requestType;

    // 账号体系：邮箱，电话，账号，id
    private String searchKey;

    // 昵称
    private String username;

    // 兴趣爱好
    private String interest;
    // 职业
    private String job;
    // 学校
    private String campus;

    // 群类型
    private String groupType;


    // 分页信息：当前第几页
    private int currentPage;
    // 当前页多少条
    private int pageSize;
    // 一共多少条
    private int totalCounts;
    // 一共多少页
    private int totalPages;


}

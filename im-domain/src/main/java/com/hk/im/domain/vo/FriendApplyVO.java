package com.hk.im.domain.vo;


import lombok.Data;

/**
 * @author : HK意境
 * @ClassName : FriendApplyVO
 * @date : 2023/1/2 21:01
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
public class FriendApplyVO {

    private Long id;

    private UserVO sender;

    private UserVO acceptor;

    /**
     * 申请类型：1.好友申请，2.加群申请
     */
    private Integer applyType;

    private String applyInfo;

    /**
     * 状态:1.待处理，2.同意，3.拒绝
     */
    private Integer status;

    /**
     * 签收：0.未签收，1.以签收
     */
    private Integer sign;






}

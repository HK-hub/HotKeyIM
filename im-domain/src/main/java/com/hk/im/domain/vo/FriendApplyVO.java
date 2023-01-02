package com.hk.im.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

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
public class FriendApplyVO {

    private Long id;

    private Long senderId;

    private Long acceptorId;

    /**
     * 申请类型：1.好友申请，2.加群申请
     */
    @TableField(value = "apply_type")
    private Integer applyType;

    private String applyInfo;

    /**
     * 状态:1.待处理，2.同意，3.拒绝
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 签收：0.未签收，1.以签收
     */
    @TableField(value = "sign")
    private Integer sign;



}

package com.hk.im.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author : HK意境
 * @ClassName : GroupApplyVO
 * @date : 2023/2/16 21:18
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class GroupApplyVO {

    /**
     * id编号
     */
    private Long id;

    /**
     * 申请发起人
     */
    private Long senderId;

    private UserVO senderVO;

    /**
     * 申请群号
     */
    private Long groupId;

    private GroupVO groupVO;

    /**
     * 处理人id
     */
    private Long handlerId;

    /**
     * 申请类型：1.搜索加群，2.邀请入群，3.扫码加群
     */
    private Integer applyType;

    /**
     * 申请说明信息,验证信息
     */
    private String applyInfo;

    /**
     * 状态:1.待处理，2.同意，3.拒绝
     */
    private Integer status;

    /**
     * 签收状态：0.未签收，1.已签收
     */
    private Integer sign;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}

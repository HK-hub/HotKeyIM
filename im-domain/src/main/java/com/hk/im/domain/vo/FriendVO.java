package com.hk.im.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author : HK意境
 * @ClassName : FriendVO
 * @date : 2023/1/3 17:52
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
@ToString
public class FriendVO {

    /**
     * 好友关系id
     */
    private Long id;

    /**
     * 当前用户id
     */
    private Long userId;

    /**
     * 好友
     */
    private UserVO friend;

    /**
     * 好友在线状态
     */
    private Integer status;

    /**
     * 状态：0.陌生人(临时会话)，1.好友，2.黑名单，3.特别关心(置顶)，4.删除
     */
    private Integer relation;

    /**
     * 分组:default=‘默认分组’，如果不是好友，默认临时会话
     */
    private String group;

    /**
     * 备注姓名
     */
    private String remarkName;

    /**
     * 备注信息
     */
    private String remarkInfo;

}


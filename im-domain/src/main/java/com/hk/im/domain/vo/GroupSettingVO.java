package com.hk.im.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author : HK意境
 * @ClassName : GroupSettingVO
 * @date : 2023/2/12 20:00
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class GroupSettingVO {

    /**
     * 群id
     */
    private Long groupId;

    /**
     * 群号
     */
    private Long groupAccount;

    /**
     * 群定位:国家-省份-城市-区-县-镇
     */
    private String position;

    /**
     * 群人数限制:200人，500人，1000人，2000人
     */
    private Integer memberCapacity;

    /**
     * 发现群方式：1.公开群(支持搜索群名称，群号，群二维码，邀请)，2.不公开群(不支持搜索群名称，支持搜索群号，群二维码，邀请)，3.邀请制(只能通过成员邀请)
     */
    private Integer findType;

    /**
     * 加群方式：1.允许任何人everybody,2.需要验证verification, 3.不允许人加群nobody
     */
    private Integer joinType;

    /**
     * 加群问题
     */
    private String problem;

    /**
     * 加群问题答案
     */
    private String answer;

    /**
     * 全员禁言
     */
    private Integer forbidSend;

    /**
     * 是否允许临时会话
     */
    private Boolean enableTemporary;

    /**
     * 群最新公告
     */
    private String announcement;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}

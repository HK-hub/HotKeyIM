package com.hk.im.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author : HK意境
 * @ClassName : RevokeMessageExtra
 * @date : 2023/5/5 11:13
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class RevokeMessageExtra {

    /**
     * 撤回者id
     */
    private Long handlerId;

    /**
     * 撤回者昵称: 备注/昵称
     */
    private String username;

    /**
     * 撤回时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}

package com.hk.im.domain.request.group;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author : HK意境
 * @ClassName : EditMemberForbiddenStateRequest
 * @date : 2023/4/27 10:08
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class EditMemberForbiddenStateRequest {

    private Long groupId;

    /**
     * 1.禁言，2.解除禁言
     */
    private Integer mode;

    private Long memberId;

    /**
     * 禁言解除时间：
     */
    private LocalDateTime forbiddenDateTime;

    /**
     * 处理者
     */
    private Long handlerId;


}

package com.hk.im.domain.request.message;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author : HK意境
 * @ClassName : DeleteRecordsRequest
 * @date : 2023/5/6 13:19
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class DeleteRecordsRequest {

    /**
     * 操作者
     */
    private Long handlerId;

    /**
     * 待删除聊天记录
     */
    private List<Long> messageIdList;


    /**
     * 聊天类型
     */
    private Integer talkType;

}

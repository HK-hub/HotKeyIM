package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author : HK意境
 * @ClassName : IncrementalSyncRequest
 * @date : 2023/4/24 13:59
 * @description : 增量同步
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class IncrementalSyncRequest {

    /**
     * 带增量同步的消息id: MessageFlow.id
     */
    private List<Long> messageIdList;


}

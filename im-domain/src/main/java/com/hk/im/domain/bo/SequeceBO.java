package com.hk.im.domain.bo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : SequeceBO
 * @date : 2023/1/26 21:22
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class SequeceBO {

    /**
     * 群聊id, 用户群临时会话策略
     */
    private Long groupId;
    /**
     * 如果为群聊那么则此处为 groupId
     */
    private Long communicationId;

    private Long participantId;


}

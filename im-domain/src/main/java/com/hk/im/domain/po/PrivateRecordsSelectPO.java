package com.hk.im.domain.po;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : PrivateRecordsSelectPO
 * @date : 2023/2/25 14:57
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class PrivateRecordsSelectPO {

    private Long senderId;

    private Long receiverId;

    // 锚点消息id
    private Long anchorId;

    // 锚点消息sequence
    private Long sequence = 0L;

    // 从第几页开始查
    private Integer startPage;

    // 查询多少条
    private Integer limit;

}

package com.hk.im.server.common.bound.input;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : InboundMessageData
 * @date : 2023/3/22 14:46
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class InboundMessageData {

    String event;

}

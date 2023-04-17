package com.hk.im.domain.message.control;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author : HK意境
 * @ClassName : MessageAckDomain
 * @date : 2023/4/17 22:15
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class MessageAckDomain {

    private List<Long> msg_id;

    private Long receiver_id;

    private Long sender_id;


}

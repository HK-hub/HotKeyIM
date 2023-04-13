package com.hk.im.domain.request;

import lombok.Data;

import java.util.List;

/**
 * @author : HK意境
 * @ClassName : CreateRoomRequest
 * @date : 2023/3/23 10:57
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
public class CreateRoomRequest {

    private Long userId;

    private Integer talkType;

    private Long receiverId;

    // 参与者
    private List<Long> participants;

}

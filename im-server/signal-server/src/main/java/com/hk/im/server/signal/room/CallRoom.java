package com.hk.im.server.signal.room;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author : HK意境
 * @ClassName : CallRoom
 * @date : 2023/3/20 15:30
 * @description : 音视频通话房间
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class CallRoom {

    // 房间id
    private Long id;

    // 房主：
    private Long masterId;

    // 主持人：例如在分享屏幕的用户，讲解PPT等等
    private Long sharerId;

    // 房间名称
    private String name;

    // 房间描述
    private String description;

    // 房间开始时间
    private LocalDateTime createTime;

    // 房间结束时间
    private LocalDateTime endTime;

}




package com.hk.im.domain.dto;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : HK意境
 * @ClassName : ConversationMessageExtra
 * @date : 2023/3/22 15:51
 * @description : 音视频通话消息扩展
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class ConversationMessageExtra {

    // 1.语音通话，2.视频通话
    private Integer type;

    // 通话发起者
    private Long senderId;

    // 主持人: 谁在演讲，分享
    private Long sharerId;

    // 参与者
    private List<Long> participants;


    // 开始时间
    private LocalDateTime startTime;

    // 结束时间
    private LocalDateTime endTime;

    // 通话时长: 秒
    private Integer duration;

    // 通话状态：1.待处理，2.通话中，3.已拒绝，4.通话结束
    private Integer status;

    // 状态名称：对应通话状态
    private String state;

    @Getter
    public static enum ConversationStateEnum {

        WAIT("待加入",1),
        ON("通话中", 2),
        REJECTED("已拒绝", 3),
        END("通话结束", 4),
        ;
        String state;
        Integer status;


        ConversationStateEnum(String state, int status) {
            this.state = state;
            this.status = status;
        }
    }



}

package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author : HK意境
 * @ClassName : TalkRecordsRequest
 * @date : 2023/2/21 22:56
 * @description : 聊天记录获取请求
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class    TalkRecordsRequest {

    // 会话ID
    private String communicationId;

    // 当前用户ID
    private String userId;

    // 发送者id:
    private String senderId;

    // 接收者id: 好友 or 群聊
    private String receiverId;

    // 会话类型：1.私聊，2.群聊
    private Integer talkType;

    // 消息类型：按照消息类型进行筛选
    private Integer msgTypes;

    // 查询锚点：参数表示查询锚点，也就是作为查询起点的那条消息，查询结果不包含锚点。不能设置为null
    private String anchor;

    // 锚点 sequence序列
    private Long sequence;

    // 查询方向：1.当前记录节点向前查找，2.向后查找；默认：向前查找
    private Integer direction = 1;

    // 当前多少页
    private Integer currentPage;

    // 请求目标页
    private Integer requestPage;

    // 需要多少页
    private Integer needPages;


    // 时间区间查找：开始时间
    private LocalDateTime fromTime;

    // 时间区间查找：结束时间
    private LocalDateTime toTime;

    // 一页多少记录
    private Integer limit = 30;

}

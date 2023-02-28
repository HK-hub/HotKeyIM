package com.hk.im.domain.dto;

import com.hk.im.domain.vo.MessageVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author : HK意境
 * @ClassName : LatestMessageRecordDTO
 * @date : 2023/2/28 12:40
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class LatestMessageRecordDTO {

    // 聊天记录
    private List<MessageVO> messageVOList;

    // 消息锚点
    private Long anchorId;

    // 消息锚点 sequence
    private Long sequence;




}

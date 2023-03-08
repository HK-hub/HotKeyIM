package com.hk.im.domain.bo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : MessageRecordMemberBO
 * @date : 2023/3/8 12:59
 * @description : 消息聊天记录中的User：发送者，接收者
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class MessageRecordMemberBO {

    private Integer talkType;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long groupId;

    private String avatar;

    private String username;

    private String remarkName;
}

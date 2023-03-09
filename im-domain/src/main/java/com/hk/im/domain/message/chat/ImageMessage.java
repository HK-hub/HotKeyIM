package com.hk.im.domain.message.chat;

import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.message.WebSocketMessage;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : HK意境
 * @ClassName : ImageMessage
 * @date : 2023/3/9 16:52
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class ImageMessage extends WebSocketMessage {

    private Integer talkType;

    private String senderId;

    private String receiverId;

    private MultipartFile image;

    @Override
    public Integer getChatMessageType() {
        return MessageConstants.ChatMessageType.IMAGE.ordinal();
    }


}

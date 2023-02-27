package com.hk.im.infrastructure.mapstruct;

import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.entity.ChatMessage;
import com.hk.im.domain.entity.MessageFlow;
import com.hk.im.domain.vo.FriendVO;
import com.hk.im.domain.vo.MessageVO;
import com.hk.im.domain.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author : HK意境
 * @ClassName : MessageMapStructure
 * @date : 2023/2/22 11:23
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Mapper
public interface MessageMapStructure {

    public static MessageMapStructure INSTANCE = Mappers.getMapper(MessageMapStructure.class);

    @Mapping(source = "message.id", target = "id")
    @Mapping(source = "message.id", target = "messageId")
    @Mapping(source = "flow.id", target = "messageFlowId")
    @Mapping(source = "message.messageType", target = "messageType")
    @Mapping(source = "message.sequence", target = "sequence")
    @Mapping(source = "message.createTime", target = "createTime")
    @Mapping(source = "message.updateTime", target = "updateTime")
    @Mapping(source = "message.deleted", target = "deleted")
    public MessageBO toBO(MessageFlow flow, ChatMessage message);


    @Mapping(source = "messageBO.id", target = "id")
    @Mapping(source = "messageBO.groupId", target = "groupId")
    @Mapping(source = "userVO", target = "sender")
    @Mapping(source = "friendVO", target = "receiver")
    @Mapping(source = "userVO.miniAvatar", target = "avatar")
    @Mapping(source = "userVO.miniAvatar", target = "senderAvatar")
    @Mapping(source = "friendVO.avatar", target = "receiverAvatar")
    @Mapping(source = "friendVO.remarkName", target = "friendRemark")
    @Mapping(source = "friendVO.nickname", target = "nickname")
    public MessageVO boToVO(MessageBO messageBO, UserVO userVO, FriendVO friendVO);

}

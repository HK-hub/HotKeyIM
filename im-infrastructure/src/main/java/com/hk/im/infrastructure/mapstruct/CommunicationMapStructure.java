package com.hk.im.infrastructure.mapstruct;

/**
 * @author : HK意境
 * @ClassName : CommunicationMapStructure
 * @date : 2023/2/17 14:38
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */

import com.hk.im.domain.entity.ChatCommunication;
import com.hk.im.domain.vo.ChatCommunicationVO;
import com.hk.im.domain.vo.FriendVO;
import com.hk.im.domain.vo.GroupVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommunicationMapStructure {

    public static CommunicationMapStructure INSTANCE = Mappers.getMapper(CommunicationMapStructure.class);

    @Mapping(source = "communication.id", target = "id")
    @Mapping(source = "communication.groupId", target = "groupId")
    @Mapping(source = "communication.top", target = "top")
    @Mapping(source = "communication.robot", target = "robot")
    @Mapping(source = "communication.disturb", target = "disturb")
    @Mapping(source = "communication.online", target = "online")
    @Mapping(source = "communication.createTime", target = "createTime")
    @Mapping(source = "friendVO", target = "friendVO")
    @Mapping(source = "groupVO", target = "groupVO")
    public ChatCommunicationVO toVO(ChatCommunication communication, FriendVO friendVO, GroupVO groupVO);

}

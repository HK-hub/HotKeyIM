package com.hk.im.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hk.im.domain.entity.ChatCommunication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Entity com.hk.im.domain.entity.ChatCommunication
 */
@Mapper
public interface ChatCommunicationMapper extends BaseMapper<ChatCommunication> {


    /**
     * 查询一个会话
     * @param senderId
     * @param receiverId
     * @return
     */
    @Select("select * from tb_chat_communication where sender_id = #{senderId} and receiver_id = #{receiverId}")
    public ChatCommunication selectCommunication(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

    @Update("update tb_chat_communication set unread_count = unread_count + #{increase} where id = #{id}")
    Boolean increaseTalkUnreadCount(@Param("id") Long id, @Param("increase") int increase);
}





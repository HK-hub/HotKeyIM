package com.hk.im.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hk.im.domain.entity.MessageFlow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Entity com.hk.im.domain.entity.MessageFlow
 */
@Mapper
public interface MessageFlowMapper extends BaseMapper<MessageFlow> {

    /**
     * 查询好友私聊最大会话消息
     * @param senderId
     * @param receiverId
     * @return
     */
    @Select("select * from tb_message_flow where (sender_id = #{senderId} and receiver_id = #{receiverId}) or " +
            "(sender_id = #{receiverId} and receiver_id = #{senderId})) order by sequence desc limit 1")
    MessageFlow selectPrivateMaxMessageSequence(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);


    /**
     * 查询群聊最大会话消息
     * @param senderId
     * @param receiverId
     * @return
     */
    @Select("select * from tb_message_flow where receiver_id = #{receiverId} order by sequence desc limit 1")
    MessageFlow selectGroupMaxMessageSequence(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

    /**
     * 获取最新的一条消息
     * @param senderId
     * @param receiverId
     * @return
     */
    @Select("select * from tb_message_flow where sender_id = #{senderId} " +
            "and receiver_id = #{receiverId} order by sequence desc limit 1")
    MessageFlow getPrivateLatestMessageRecord(@Param("senderId") String senderId, @Param("receiverId") String receiverId);
}





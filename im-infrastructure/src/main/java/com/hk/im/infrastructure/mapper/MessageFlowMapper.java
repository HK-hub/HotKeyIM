package com.hk.im.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hk.im.domain.entity.MessageFlow;
import com.hk.im.domain.po.PrivateRecordsSelectPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import javax.swing.*;
import java.util.List;

/**
 * @Entity com.hk.im.domain.entity.MessageFlow
 */
@Mapper
public interface MessageFlowMapper extends BaseMapper<MessageFlow> {


    /**
     * 从锚点开始获取私聊聊天消息，分页
     * @param po
     * @return
     */
    List<MessageFlow> selectPrivateRecordsByAnchor(@Param("po") PrivateRecordsSelectPO po);


    /**
     * 从锚点开始查询群聊聊天消息分页
     * @param po
     * @return
     */
    List<MessageFlow> selectGroupRecordsByAnchor(@Param("po") PrivateRecordsSelectPO po);


    /**
     * 查询好友私聊最大会话消息
     * @param senderId
     * @param receiverId
     * @return
     */
    @Select("select * from tb_message_flow where (sender_id = #{senderId} and receiver_id = #{receiverId}) or " +
            "(sender_id = #{receiverId} and receiver_id = #{senderId}) order by sequence desc limit 1")
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





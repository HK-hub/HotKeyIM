package com.hk.im.infrastructure.mapper;

import com.hk.im.domain.entity.Sequence;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Entity com.hk.im.domain.entity.Sequence
 */
public interface SequenceMapper extends BaseMapper<Sequence> {

    /**
     * 获取会话sequence
     * @param senderId
     * @param receiverId
     * @return
     */
    @Select("select * from tb_sequence where (senderId = #{senderId} and receiverId = #{receiverId}) " +
            "or (senderId = #{receiverId} and receiverId = #{senderId})) limit 1")
    Sequence selectSessionSequence(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
}





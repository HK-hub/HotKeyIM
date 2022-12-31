package com.hk.im.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hk.im.domain.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Entity com.hk.im.domain.entity.ChatMessage
 */
@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {

}





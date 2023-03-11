package com.hk.im.service.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.MessageFileService;
import com.hk.im.domain.entity.MessageFile;
import com.hk.im.infrastructure.mapper.MessageFileMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class MessageFileServiceImpl extends ServiceImpl<MessageFileMapper, MessageFile>
    implements MessageFileService {

}





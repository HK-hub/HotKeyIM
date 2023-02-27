package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.CommentService;
import com.hk.im.domain.entity.Comment;
import com.hk.im.infrastructure.mapper.CommentMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService {

}





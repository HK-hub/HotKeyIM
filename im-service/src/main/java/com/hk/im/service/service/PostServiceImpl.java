package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.PostService;
import com.hk.im.domain.entity.Post;
import com.hk.im.infrastructure.mapper.PostMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
    implements PostService {

}





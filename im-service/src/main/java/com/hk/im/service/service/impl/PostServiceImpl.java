package com.hk.im.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.domain.entity.Post;
import com.hk.im.service.service.PostService;
import com.hk.im.infrastructure.mapper.PostMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
    implements PostService {

}





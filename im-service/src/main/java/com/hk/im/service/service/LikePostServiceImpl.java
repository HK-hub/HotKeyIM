package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.LikePostService;
import com.hk.im.domain.entity.LikePost;
import com.hk.im.infrastructure.mapper.LikePostMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class LikePostServiceImpl extends ServiceImpl<LikePostMapper, LikePost>
    implements LikePostService {

}





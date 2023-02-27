package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.UserCloudResourceService;
import com.hk.im.domain.entity.UserCloudResource;
import com.hk.im.infrastructure.mapper.UserCloudResourceMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class UserCloudResourceServiceImpl extends ServiceImpl<UserCloudResourceMapper, UserCloudResource>
    implements UserCloudResourceService {

}





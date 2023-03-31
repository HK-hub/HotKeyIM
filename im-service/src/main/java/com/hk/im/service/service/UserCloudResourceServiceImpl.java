package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.CloudResourceService;
import com.hk.im.client.service.UserCloudResourceService;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.entity.CloudResource;
import com.hk.im.domain.entity.UserCloudResource;
import com.hk.im.infrastructure.mapper.UserCloudResourceMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName : UserCloudResourceServiceImpl
 * @author : HK意境
 * @date : 2023/3/31 13:51
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class UserCloudResourceServiceImpl extends ServiceImpl<UserCloudResourceMapper, UserCloudResource>
    implements UserCloudResourceService {

    @Resource
    private CloudResourceService cloudResourceService;

}





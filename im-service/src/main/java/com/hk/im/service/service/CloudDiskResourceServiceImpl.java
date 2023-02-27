package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.CloudDiskResourceService;
import com.hk.im.domain.entity.CloudDiskResource;
import com.hk.im.infrastructure.mapper.CloudDiskResourceMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class CloudDiskResourceServiceImpl extends ServiceImpl<CloudDiskResourceMapper, CloudDiskResource>
    implements CloudDiskResourceService {

}





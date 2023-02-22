package com.hk.im.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.domain.entity.UserCollection;
import com.hk.im.service.service.UserCollectionService;
import com.hk.im.infrastructure.mapper.UserCollectionMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class UserCollectionServiceImpl extends ServiceImpl<UserCollectionMapper, UserCollection>
    implements UserCollectionService{

}





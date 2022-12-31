package com.hk.im.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.domain.entity.UserInfo;
import com.hk.im.infrastructure.mapper.UserInfoMapper;
import com.hk.im.service.service.UserInfoService;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService {

}





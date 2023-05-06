package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.UserDeleteMessageService;
import com.hk.im.domain.entity.UserDeleteMessage;
import com.hk.im.infrastructure.mapper.UserDeleteMessageMapper;
import org.springframework.stereotype.Service;

/**
 * @ClassName : UserDeleteMessageServiceImpl
 * @author : HK意境
 * @date : 2023/5/6 13:38
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class UserDeleteMessageServiceImpl extends ServiceImpl<UserDeleteMessageMapper, UserDeleteMessage>
    implements UserDeleteMessageService {

}





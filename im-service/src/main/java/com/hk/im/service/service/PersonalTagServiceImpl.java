package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.PersonalTagService;
import com.hk.im.domain.entity.PersonalTag;
import com.hk.im.infrastructure.mapper.PersonalTagMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class PersonalTagServiceImpl extends ServiceImpl<PersonalTagMapper, PersonalTag>
    implements PersonalTagService {

}





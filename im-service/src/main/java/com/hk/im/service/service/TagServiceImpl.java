package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.TagService;
import com.hk.im.domain.entity.Tag;
import com.hk.im.infrastructure.mapper.TagMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService {

}





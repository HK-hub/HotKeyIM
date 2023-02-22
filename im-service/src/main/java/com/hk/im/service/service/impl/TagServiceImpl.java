package com.hk.im.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.domain.entity.Tag;
import com.hk.im.service.service.TagService;
import com.hk.im.infrastructure.mapper.TagMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

}





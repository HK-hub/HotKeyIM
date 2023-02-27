package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.NoteTagService;
import com.hk.im.domain.entity.NoteTag;
import com.hk.im.infrastructure.mapper.NoteTagMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class NoteTagServiceImpl extends ServiceImpl<NoteTagMapper, NoteTag>
    implements NoteTagService {

}





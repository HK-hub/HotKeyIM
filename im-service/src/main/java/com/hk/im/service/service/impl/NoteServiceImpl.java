package com.hk.im.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.domain.entity.Note;
import com.hk.im.service.service.NoteService;
import com.hk.im.infrastructure.mapper.NoteMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note>
    implements NoteService {

}





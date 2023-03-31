package com.hk.im.service.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.NoteAnnexService;
import com.hk.im.domain.entity.NoteAnnex;
import com.hk.im.infrastructure.mapper.NoteAnnexMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : NoteAnnexServiceImpl
 * @author : HK意境
 * @date : 2023/3/31 14:04
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class NoteAnnexServiceImpl extends ServiceImpl<NoteAnnexMapper, NoteAnnex>
    implements NoteAnnexService {

    /**
     * 获取笔记附件列表
     * @param noteId
     * @return
     */
    @Override
    public List<NoteAnnex> getNoteAnnexList(Long noteId) {

        List<NoteAnnex> list = this.lambdaQuery()
                .eq(NoteAnnex::getNoteId, noteId)
                .eq(NoteAnnex::getDeleted, Boolean.FALSE)
                .list();

        return list;
    }
}





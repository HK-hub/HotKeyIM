package com.hk.im.service.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.hk.im.client.service.NoteAnnexService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.NoteAnnex;
import com.hk.im.infrastructure.mapper.NoteAnnexMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : HK意境
 * @ClassName : NoteAnnexServiceImpl
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
     *
     * @param noteId
     *
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


    /**
     * 彻底删除笔记附件
     *
     * @param articleId
     *
     * @return
     */
    @Override
    public boolean removeNoteAnnexs(Long articleId) {

        boolean remove = this.lambdaUpdate()
                .eq(NoteAnnex::getNoteId, articleId)
                .remove();
        return remove;
    }

    /**
     * 讲附件放入回收站
     *
     * @param articleId
     *
     * @return
     */
    @Override
    public boolean putNoteAnnexToRecycle(Long articleId) {

        boolean update = this.lambdaUpdate()
                .eq(NoteAnnex::getNoteId, articleId)
                .eq(NoteAnnex::getDeleted, Boolean.FALSE)
                .set(NoteAnnex::getDeleted, Boolean.TRUE)
                .update();
        return update;
    }


    /**
     * 获取回收站附件列表
     * @return
     */
    @Override
    public ResponseResult getNoteRecycleAnnexList() {


        return ResponseResult.SUCCESS(Lists.newArrayList());
    }


}





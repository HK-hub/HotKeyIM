package com.hk.im.client.service;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.NoteAnnex;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface NoteAnnexService extends IService<NoteAnnex> {


    /**
     * 获取笔记附件列表
     * @param noteId
     * @return
     */
    public List<NoteAnnex> getNoteAnnexList(Long noteId);


    /**
     * 彻底删除笔记附件
     * @param articleId
     * @return
     */
    boolean removeNoteAnnexs(Long articleId);

    /**
     * 讲附件放入回收站
     * @param articleId
     * @return
     */
    boolean putNoteAnnexToRecycle(Long articleId);


    /**
     * 获取回收站附件列表
     * @return
     */
    ResponseResult getNoteRecycleAnnexList();


}

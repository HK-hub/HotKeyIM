package com.hk.im.client.service;

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



}

package com.hk.im.client.service;

import com.hk.im.domain.entity.NoteTag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @ClassName : NoteTagService
 * @author : HK意境
 * @date : 2023/3/27 16:13
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface NoteTagService extends IService<NoteTag> {

    /**
     * 获取指定笔记的标签集合
     * @param noteId
     * @return
     */
    List<NoteTag> getNoteTagList(Long noteId);

    /**
     * 获取指定标签下的笔记集合
     * @param tagId
     * @return
     */
    List<NoteTag> getNoteListByTagId(Long tagId);
}

package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.NoteTagService;
import com.hk.im.domain.entity.NoteTag;
import com.hk.im.infrastructure.mapper.NoteTagMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : NoteTagServiceImpl
 * @author : HK意境
 * @date : 2023/3/27 16:14
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class NoteTagServiceImpl extends ServiceImpl<NoteTagMapper, NoteTag>
    implements NoteTagService {


    /**
     * 获取用户笔记标签列表
     * @param noteId
     * @return
     */
    @Override
    public List<NoteTag> getNoteTagList(Long noteId) {

        return null;
    }


    /**
     * 获取指定标签下的笔记集合
     * @param tagId
     * @return
     */
    @Override
    public List<NoteTag> getNoteListByTagId(Long tagId) {

        List<NoteTag> noteTagList = this.lambdaQuery()
                .eq(NoteTag::getTagId, tagId)
                .list();

        return noteTagList;
    }
}





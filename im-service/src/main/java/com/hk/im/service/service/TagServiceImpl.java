package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.NoteTagService;
import com.hk.im.client.service.TagService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.entity.NoteTag;
import com.hk.im.domain.entity.Tag;
import com.hk.im.infrastructure.mapper.TagMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.*;
import java.util.List;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : TagServiceImpl
 * @date : 2023/3/27 16:11
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Resource
    private NoteTagService noteTagService;


    /**
     * 获取用户笔记tag列表
     *
     * @return
     */
    @Override
    public ResponseResult getUserNoteTags(Long userId) {

        // 参数校验
        if (Objects.isNull(userId)) {
            userId = UserContextHolder.get().getId();
        }

        List<Tag> tagList = this.lambdaQuery()
                .eq(Tag::getUserId, userId)
                .eq(Tag::getDeleted, false)
                .list();

        return ResponseResult.SUCCESS(tagList);
    }


    /**
     * 获取笔记文章标签列表
     *
     * @param noteId
     *
     * @return
     */
    @Override
    public List<Tag> getNoteTagList(Long noteId) {

        // 获取标签id列表集合
        List<NoteTag> noteTagList = this.noteTagService.lambdaQuery()
                .eq(NoteTag::getNoteId, noteId)
                .list();
        List<Long> tagIdList = noteTagList.stream().map(NoteTag::getTagId).toList();

        // 获取标签
        List<Tag> tagList = tagIdList.stream().map(this::getById).toList();

        return tagList;
    }
}





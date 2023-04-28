package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.NoteTagService;
import com.hk.im.client.service.TagService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.entity.NoteTag;
import com.hk.im.domain.entity.Tag;
import com.hk.im.domain.request.EditNoteTagRequest;
import com.hk.im.domain.vo.TagVO;
import com.hk.im.infrastructure.mapper.TagMapper;
import com.hk.im.infrastructure.mapstruct.NoteTagMapStructure;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.swing.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        // 查询list
        List<Tag> tagList = this.lambdaQuery()
                .eq(Tag::getUserId, userId)
                .eq(Tag::getDeleted, false)
                .list();

        // 转换为 NoteVO
        List<TagVO> tagVOS = tagList.stream().map(tag -> {
            // 查询tag 标签对应的文章数量
            int count = this.getTagMappingArticleCount(tag.getId());
            return NoteTagMapStructure.INSTANCE.toVO(tag, count);
        }).collect(Collectors.toList());

        return ResponseResult.SUCCESS(tagVOS);
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
        List<Long> tagIdList = noteTagList.stream().map(NoteTag::getTagId).collect(Collectors.toList());

        // 获取标签
        List<Tag> tagList = tagIdList.stream().map(this::getById).collect(Collectors.toList());

        return tagList;
    }


    /**
     * 编辑标签
     * @param request
     * @return
     */
    @Override
    public ResponseResult editUserNoteTag(EditNoteTagRequest request) {

        // 参数校验
        if (Objects.isNull(request) || StringUtils.isEmpty(request.getName()) || Objects.isNull(request.getTagId())) {
            // 校验失败
            return ResponseResult.FAIL("参数校验失败!");
        }

        // 素材
        Long userId = UserContextHolder.get().getId();
        Long tagId = request.getTagId();

        // 查询标签是否存在
        Tag tag = this.getById(tagId);
        boolean res = false;

        if (Objects.isNull(tag)) {
            // 标签不存在，创建
            tag = new Tag()
                    .setUserId(userId)
                    .setName(request.getName())
                    .setDescription(request.getDescription())
                    .setAvatar(request.getAvatar());
            res = this.save(tag);

        } else {
            // 标签存在，进行更新
            tag.setName(request.getName());
            res = this.updateById(tag);
        }

        // 构建返回结果
        if (BooleanUtils.isFalse(res)) {
            return ResponseResult.FAIL("编辑标签失败!");
        }

        // 编辑成功
        return ResponseResult.SUCCESS(tag);
    }


    /**
     * 添加标签给笔记文章
     * @param tagId
     * @param articleId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addTagToArticle(Long tagId, Long articleId) {

        NoteTag noteTag = new NoteTag()
                .setTagId(tagId)
                .setNoteId(articleId);

        return this.noteTagService.save(noteTag);
    }


    /**
     * 删除标签
     * @param tagId
     * @return
     */
    @Override
    public ResponseResult deleteTag(Long tagId) {

        // 参数校验
        if (Objects.isNull(tagId)) {
            return ResponseResult.FAIL("标签不存在!");
        }

        // 查看该标签是否还在使用
        Long count = this.noteTagService.lambdaQuery()
                .eq(NoteTag::getTagId, tagId)
                .count();

        if (count > 0) {
            // 标签还在使用不能删除
            return ResponseResult.FAIL().setDataAsMessage("标签还在使用中,无法删除!");
        }

        // 删除
        boolean remove = this.removeById(tagId);

        return ResponseResult.SUCCESS(remove);
    }


    /**
     * 获取标签对应文章数量
     * @param tagId
     * @return
     */
    public int getTagMappingArticleCount(Long tagId) {
        Long count = this.noteTagService.lambdaQuery()
                .eq(NoteTag::getTagId, tagId)
                .count();
        return count.intValue();
    }


}





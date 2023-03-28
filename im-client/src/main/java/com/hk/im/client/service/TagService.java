package com.hk.im.client.service;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.domain.request.EditNoteTagRequest;

import java.util.List;

/**
 * @ClassName : TagService
 * @author : HK意境
 * @date : 2023/3/27 17:28
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface TagService extends IService<Tag> {

    /**
     * 获取用户笔记tag 列表
     * @return
     */
    ResponseResult getUserNoteTags(Long userId);

    /**
     * 获取笔记文章标签列表
     * @param id
     * @return
     */
    List<Tag> getNoteTagList(Long id);

    /**
     * 编辑文集标签
     * @param request
     * @return {@link Tag}
     */
    ResponseResult editUserNoteTag(EditNoteTagRequest request);
}

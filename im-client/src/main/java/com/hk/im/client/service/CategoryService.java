package com.hk.im.client.service;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.domain.entity.Note;
import com.hk.im.domain.request.EditNoteCategoryRequest;
import com.hk.im.domain.vo.NoteVO;

import java.util.List;

/**
 * @ClassName : CategoryService
 * @author : HK意境
 * @date : 2023/2/22 21:25
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface CategoryService extends IService<Category> {


    /**
     * 获取用户笔记分类列表
     *
     * @param userId
     *
     * @return
     */
    ResponseResult getUserNoteCategoryList(Long userId);


    /**
     * 添加或修改笔记分类列表
     *
     * @param request
     *
     * @return
     */
    ResponseResult editNoteCategoryList(EditNoteCategoryRequest request);

    /**
     * 批量获取笔记的分类
     *
     * @param noteList
     *
     * @return
     */
    List<NoteVO> batchGetNoteCategory(List<Note> noteList);


    /**
     * 获取笔记分类
     * @param note
     * @return {@link Category}
     */
    public Category getNoteCategory(Long noteId);

}

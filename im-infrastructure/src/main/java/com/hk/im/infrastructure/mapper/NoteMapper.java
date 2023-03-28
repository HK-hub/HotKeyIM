package com.hk.im.infrastructure.mapper;

import com.hk.im.domain.entity.Note;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hk.im.domain.request.GetArticleListRequest;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * @Entity com.hk.im.domain.entity.Note
 */
public interface NoteMapper extends BaseMapper<Note> {

    // 查询用户文集列表
    List<Note> selectNoteArticleList(@Param("request") GetArticleListRequest request);


    /**
     * 近期编辑
     * @param request
     * @return
     */
    List<Note> selectRecentEditNoteList(GetArticleListRequest request, @Param("startTime") LocalDate startTime);

    /**
     * 我的收藏
     * @param noteIdList
     * @param request
     * @return
     */
    List<Note> selectCollectedNoteList(@Param("noteIdList") List<Long> noteIdList, @Param("request") GetArticleListRequest request);

    /**
     * 笔记分类
     * @param request
     * @return
     */
    List<Note> selectNoteListByCategory(GetArticleListRequest request);

    /**
     * 标签分类
     * @param noteIdList
     * @param request
     * @return
     */
    List<Note> selectNoteListByTag(@Param("noteIdList") List<Long> noteIdList, @Param("request") GetArticleListRequest request);
}





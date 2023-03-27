package com.hk.im.infrastructure.mapper;

import com.hk.im.domain.entity.Note;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hk.im.domain.request.GetArticleListRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.hk.im.domain.entity.Note
 */
public interface NoteMapper extends BaseMapper<Note> {

    // 查询用户文集列表
    List<Note> selectNoteArticleList(@Param("request") GetArticleListRequest request);
}





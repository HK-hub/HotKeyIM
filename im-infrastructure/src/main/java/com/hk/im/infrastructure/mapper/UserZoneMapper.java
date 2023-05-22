package com.hk.im.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hk.im.domain.entity.UserZone;
import com.hk.im.domain.request.zone.GetObservableNotesRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.hk.im.domain.entity.UserZone
 */
public interface UserZoneMapper extends BaseMapper<UserZone> {

    /**
     * 查询已经发布的笔记说说
     * @param userId
     * @param noteId
     * @return
     */
    UserZone selectPublishedNote(@Param("userId") Long userId, @Param("noteId") Long noteId);


    /**
     * 查询用户发布的说说列表
     * @param userId
     * @return
     */
    List<UserZone> selectUserPublishedNoteList(Long userId);

    /**
     * 查询用户能够查看到的说说列表
     * @param request
     * @param friendIdList
     * @return
     */
    List<UserZone> selectObservableZoneNoteList(@Param("request") GetObservableNotesRequest request,
                                                @Param("friendIdList") List<Long> friendIdList);
}





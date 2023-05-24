package com.hk.im.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.UserZone;
import com.hk.im.domain.request.zone.EditNoteRequest;
import com.hk.im.domain.request.zone.GetObservableNotesRequest;

import java.util.List;

/**
 * @ClassName : UserZoneService
 * @author : HK意境
 * @date : 2023/5/22 11:25
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface UserZoneService extends IService<UserZone> {

    /**
     * 获取发布的说说
     * @param userId
     * @param noteId
     * @return
     */
    UserZone getPublishedNote(Long userId, Long noteId);

    /**
     * 获取用户发布的说说列表
     * @param userId
     * @return
     */
    List<UserZone> getUserPublishedNotes(Long userId);

    /**
     * 获取用户可以查看到的空间说说
     * @param request
     * @return
     */
    ResponseResult getObservableNotes(GetObservableNotesRequest request);


    /**
     * 编辑说说评论
     * @param request
     * @return
     */
    ResponseResult editNoteComment(EditNoteRequest request);
}

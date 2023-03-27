package com.hk.im.client.service;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.Note;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.domain.request.GetArticleListRequest;

/**
 * @ClassName : NoteService
 * @author : HK意境
 * @date : 2023/3/27 17:22
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface NoteService extends IService<Note> {

    /**
     * 获取用户文集列表
     * @param request
     * @return
     */
    ResponseResult getNoteArticleList(GetArticleListRequest request);
}

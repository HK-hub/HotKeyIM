package com.hk.im.client.service;

import com.hk.im.domain.entity.UserCollection;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @ClassName : UserCollectionService
 * @author : HK意境
 * @date : 2023/3/28 14:03
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface UserCollectionService extends IService<UserCollection> {


    /**
     * 获取用户收藏的笔记文集列表
     * @param userId
     * @return
     */
    public List<UserCollection> getUserCollectedNoteList(Long userId);


    /**
     * 通过id获取用户收藏的笔记文章
     * @param collectableId
     * @return {@link UserCollection}
     */
    UserCollection getUserCollectedNoteById(Long collectableId);


    /**
     * 取消收藏笔记
     * @param articleId
     * @return
     */
    Boolean removeTheCollectedNote(Long articleId);


    /**
     * 收藏笔记
     * @param articleId
     * @return
     */
    UserCollection collectTheNoteArticle(Long articleId);
}

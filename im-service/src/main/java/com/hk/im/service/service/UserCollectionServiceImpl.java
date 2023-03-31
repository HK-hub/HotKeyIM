package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.UserCollectionService;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.entity.UserCollection;
import com.hk.im.infrastructure.mapper.UserCollectionMapper;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @ClassName : UserCollectionServiceImpl
 * @author : HK意境
 * @date : 2023/3/28 14:04
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class UserCollectionServiceImpl extends ServiceImpl<UserCollectionMapper, UserCollection>
    implements UserCollectionService {

    /**
     * 获取用户收藏的笔记文集列表
     * @param userId
     * @return
     */
    @Override
    public List<UserCollection> getUserCollectedNoteList(Long userId) {

        List<UserCollection> collectionList = this.lambdaQuery()
                .eq(UserCollection::getUserId, userId)
                .eq(UserCollection::getType, UserCollection.CollectableType.note.ordinal())
                .list();
        return collectionList;
    }


    /**
     * 根据id获取用户收藏的笔记文章
     * @param collectableId
     * @return
     */
    @Override
    public UserCollection getUserCollectedNoteById(Long collectableId) {

        Long userId = UserContextHolder.get().getId();
        UserCollection collection = this.lambdaQuery()
                .eq(UserCollection::getUserId, userId)
                .eq(UserCollection::getCollectibleId, collectableId)
                .eq(UserCollection::getType, UserCollection.CollectableType.note.ordinal())
                .eq(UserCollection::getDeleted, Boolean.FALSE)
                .one();

        return collection;
    }


    /**
     * 取消收藏笔记
     * @param articleId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeTheCollectedNote(Long articleId) {

        Long userId = UserContextHolder.get().getId();
        boolean remove = this.lambdaUpdate()
                .eq(UserCollection::getUserId, userId)
                .eq(UserCollection::getCollectibleId, articleId)
                .eq(UserCollection::getType, UserCollection.CollectableType.note.ordinal())
                .remove();

        return remove;
    }


    /**
     * 收藏笔记
     * @param articleId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserCollection collectTheNoteArticle(Long articleId) {

        // 查询是否存在
        Long userId = UserContextHolder.get().getId();
        UserCollection collection = this.lambdaQuery()
                .eq(UserCollection::getUserId, userId)
                .eq(UserCollection::getCollectibleId, articleId)
                .eq(UserCollection::getType, UserCollection.CollectableType.note.ordinal())
                .eq(UserCollection::getDeleted, Boolean.FALSE)
                .one();

        if (Objects.nonNull(collection)) {
            // 已经收藏成功了
            return collection;
        }

        // 未收藏进行收藏
        collection = new UserCollection()
                .setCollectibleId(articleId)
                .setUserId(userId)
                .setType(UserCollection.CollectableType.note.ordinal());

        boolean save = this.save(collection);

        if (BooleanUtils.isFalse(save)) {
            // 保存失败
            return null;
        }

        // 收藏成功
        return collection;
    }
}





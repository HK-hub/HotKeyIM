package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.UserCollectionService;
import com.hk.im.domain.entity.UserCollection;
import com.hk.im.infrastructure.mapper.UserCollectionMapper;
import org.springframework.stereotype.Service;

import java.util.List;

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
}





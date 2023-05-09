package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.CollectEmoticonService;
import com.hk.im.domain.entity.CollectEmoticon;
import com.hk.im.infrastructure.mapper.CollectEmoticonMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class CollectEmoticonServiceImpl extends ServiceImpl<CollectEmoticonMapper, CollectEmoticon>
    implements CollectEmoticonService {


    /**
     * 获取用户收藏表情包
     * @param userId
     * @param emoticonId
     * @return
     */
    @Override
    public CollectEmoticon getUserCollectEmoticon(Long userId, Long emoticonId) {

        CollectEmoticon collectEmoticon = this.lambdaQuery()
                .eq(CollectEmoticon::getUserId, userId)
                .eq(CollectEmoticon::getEmoticonId, emoticonId)
                .one();

        return collectEmoticon;
    }


    /**
     * 移除用户收藏表情包
     * @param collectEmoticon
     * @return
     */
    @Override
    public boolean removeUserCollectEmoticon(CollectEmoticon collectEmoticon) {

        boolean remove = this.lambdaUpdate()
                .eq(CollectEmoticon::getUserId, collectEmoticon.getUserId())
                .eq(CollectEmoticon::getEmoticonId, collectEmoticon.getEmoticonId())
                .remove();

        return remove;
    }



}




